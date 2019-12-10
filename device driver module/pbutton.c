#include <linux/module.h>	// needed by all modules
#include <linux/kernel.h>	// needed by kernel functions
#include <linux/init.h>		// needed by module macros
#include <linux/uaccess.h>	// needed by user space copy functions
#include <linux/fs.h>		// needed by file operations
#include <linux/miscdevice.h>	// needed by misc device driver functions
#include <asm/ioctl.h>		// needed by ioctl functions and macros

#define DRIVER_AUTHOR	"CAUSW SHINYEONG YUN"
#define DRIVER_DESC	"driver for pbutton(push buttons)"

#define PBUTTON_NAME		"pbutton"
#define PBUTTON_MODULE_VERSION	"pbutton V1.0"
#define PBUTTON_ADDR		0x050


// gpio fpga interface provided
extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);


// global
static int pbutton_in_use = 0;


int pbutton_open(struct inode *pinode, struct file *pfile)
{
	if (pbutton_in_use != 0) {
		return -EBUSY;
	}

	pbutton_in_use = 1;

	return 0;
}


int pbutton_release(struct inode *pinode, struct file *pfile)
{
	pbutton_in_use = 0;

	return 0;
}


ssize_t pbutton_read(struct file *pinode, char *gdata, size_t len, loff_t *off_what) {
	unsigned short wordvalue;
	char *tmp = NULL;
	char i;

	tmp = gdata;

	for (i = 0; i < 9; i++) {
	    wordvalue = iom_fpga_itf_read((unsigned int) PBUTTON_ADDR + (i*2));
	    if (wordvalue == 1)
		    break;
	}
	i++;	
	
	if (i == 10) {
	    i = 0;
	}

	if (copy_to_user(tmp, &i, 1)) {
		return -EFAULT;
	}


	return len;
}

static struct file_operations pbutton_fops = {
	.owner	= THIS_MODULE,
	.open	= pbutton_open,
	.read   = pbutton_read,
	.release= pbutton_release,
};

static struct miscdevice pbutton_driver = {
	.fops	= &pbutton_fops,
	.name	= PBUTTON_NAME,
	.minor	= MISC_DYNAMIC_MINOR,
};


int pbutton_init(void)
{
	misc_register(&pbutton_driver);
	printk(KERN_INFO "driver: %s driver init\n", PBUTTON_NAME);

	return 0;
}

void pbutton_exit(void)
{
	misc_deregister(&pbutton_driver);
	printk(KERN_INFO "driver: %s driver exit\n", PBUTTON_NAME);
}

module_init(pbutton_init);
module_exit(pbutton_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");
