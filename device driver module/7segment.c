#include <linux/module.h>	// needed by all modules
#include <linux/kernel.h>	// needed by kernel functions
#include <linux/init.h>		// needed by module macros
#include <linux/uaccess.h>	// needed by user space copy functions
#include <linux/fs.h>		// needed by file operations
#include <linux/miscdevice.h>	// needed by misc device driver functions


#define DRIVER_AUTHOR	"CAUSW SHINYEONG YUN"
#define DRIVER_DESC	"driver for 7-Segment"

#define SSEG_NAME		"7segment"
#define SSEG_MODULE_VERSION	"7segment V1.0"
#define SSEG_ADDR		0x004


// gpio fpga interface provided
extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);

// global
static int sseg_in_use = 0;


int sseg_open(struct inode *pinode, struct file *pfile)
{
	if (sseg_in_use != 0) {
		return -EBUSY;
	}

	sseg_in_use = 1;

	return 0;
}

int sseg_release(struct inode *pinode, struct file *pfile)
{
	sseg_in_use = 0;

	return 0;
}

ssize_t sseg_write(struct file *pinode, const char *gdata, size_t len, loff_t *off_what)
{
	unsigned char bytevalues[4];
	unsigned short wordvalue;
	const char *tmp = NULL;

	tmp = gdata;

	if (copy_from_user(&bytevalues, tmp, 4)) {
		return -EFAULT;
	}

	wordvalue = (bytevalues[0] << 12) | (bytevalues[1] << 8) |
		(bytevalues[2] << 4) | (bytevalues[3] << 0);
	iom_fpga_itf_write((unsigned int) SSEG_ADDR, wordvalue);

	return len;
}

ssize_t sseg_read(struct file *pinode, char *gdata, size_t len, loff_t *off_what)
{
	unsigned char bytevalues[4];
	unsigned short wordvalue;
	char *tmp = NULL;

	tmp = gdata;

	wordvalue = iom_fpga_itf_read((unsigned int) SSEG_ADDR);
	bytevalues[0] = (wordvalue >> 12) & 0xF;
	bytevalues[1] = (wordvalue >> 8) & 0xF;
	bytevalues[2] = (wordvalue >> 4) & 0xF;
	bytevalues[3] = (wordvalue >> 0) & 0xF;

	if (copy_to_user(tmp, &bytevalues, 4)) {
		return -EFAULT;
	}

	return len;
}

static struct file_operations sseg_fops = {
	.owner	= THIS_MODULE,
	.open	= sseg_open,
	.read	= sseg_read,
	.write	= sseg_write,
	.release= sseg_release,
};

static struct miscdevice sseg_driver = {
	.fops	= &sseg_fops,
	.name	= SSEG_NAME,
	.minor	= MISC_DYNAMIC_MINOR,
};

int sseg_init(void)
{
	misc_register(&sseg_driver);
	printk(KERN_INFO "driver: %s driver init\n", SSEG_NAME);

	return 0;
}

void sseg_exit(void)
{
	misc_deregister(&sseg_driver);
	printk(KERN_INFO "driver: %s driver exit\n", SSEG_NAME);
}

module_init(sseg_init);
module_exit(sseg_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");
