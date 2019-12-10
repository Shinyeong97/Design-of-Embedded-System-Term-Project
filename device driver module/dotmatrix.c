/* Chung-Ang University
 * Computer Science & Engineering
 * 2019 fall semester
 * Design of Embedded System
 * Take-Home Exam
 * Shinyeong Yun (20163657)
 * */

#include <linux/module.h>	// needed by all modules
#include <linux/kernel.h>	// needed by kernel functions
#include <linux/init.h>		// needed by module macros
#include <linux/uaccess.h>	// needed by user space copy functions
#include <linux/fs.h>		// needed by file operations
#include <linux/miscdevice.h>	// needed by misc device driver functions
#include <asm/ioctl.h>		// needed by ioctl functions and macros
#include <linux/delay.h>	// needed by kernel sleep

#define DRIVER_AUTHOR	"CAUSW SHINYEONG YUN"
#define DRIVER_DESC	"driver for dotmatrix"

#define DOTM_NAME		"dotmatrix"
#define DOTM_MODULE_VERSION	"dotmatrix V1.0"
#define DOTM_ADDR		0x210

#define DOTM_MAGIC		0xBC
#define DOTM_SET_ALL		_IOW(DOTM_MAGIC, 0, int)
#define DOTM_SET_CLEAR		_IOW(DOTM_MAGIC, 1, int)
#define DOTM_WIN		_IOW(DOTM_MAGIC, 2, int)
#define DOTM_LOSE		_IOW(DOTM_MAGIC, 3, int)
#define DOTM_SCORE		_IOW(DOTM_MAGIC, 4, int)
#define DOTM_BEAT		_IOW(DOTM_MAGIC, 5, int)
#define DOTM_FIRE		_IOW(DOTM_MAGIC, 6, int)

// gpio fpga interface provided
extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);


// global
static int dotm_in_use = 0;

// dotmatrix fonts
unsigned char dotm_fontmap_decimal[10][10] = {
        {0x3e,0x7f,0x63,0x73,0x73,0x6f,0x67,0x63,0x7f,0x3e}, // 0 head to tail
        {0x0c,0x1c,0x1c,0x0c,0x0c,0x0c,0x0c,0x0c,0x0c,0x1e}, // 1
        {0x7e,0x7f,0x03,0x03,0x3f,0x7e,0x60,0x60,0x7f,0x7f}, // 2
        {0xfe,0x7f,0x03,0x03,0x7f,0x7f,0x03,0x03,0x7f,0x7e}, // 3
        {0x66,0x66,0x66,0x66,0x66,0x66,0x7f,0x7f,0x06,0x06}, // 4
        {0x7f,0x7f,0x60,0x60,0x7e,0x7f,0x03,0x03,0x7f,0x7e}, // 5
        {0x60,0x60,0x60,0x60,0x7e,0x7f,0x63,0x63,0x7f,0x3e}, // 6
        {0x7f,0x7f,0x63,0x63,0x03,0x03,0x03,0x03,0x03,0x03}, // 7
        {0x3e,0x7f,0x63,0x63,0x7f,0x7f,0x63,0x63,0x7f,0x3e}, // 8
        {0x3e,0x7f,0x63,0x63,0x7f,0x3f,0x03,0x03,0x03,0x03} // 9
};


unsigned char dotm_fontmap_full[10] = {
        0x7f,0x7f,0x7f,0x7f,0x7f,0x7f,0x7f,0x7f,0x7f,0x7f
};


unsigned char dotm_fontmap_empty[10] = {
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
};

unsigned char dotm_fontmap_win[4][10] = {
	{0x49, 0x49, 0x49, 0x49, 0x49, 0x49, 0x49, 0x49, 0x49, 0x36},	// W
	{0x7F, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x7F},	// I
	{0x41, 0x61, 0x51, 0x51, 0x49, 0x49, 0x45, 0x45, 0x43, 0x41},	// N
	{0x1C, 0x22, 0x41, 0x55, 0x41, 0x41, 0x55, 0x49, 0x22, 0x1C}	// :)
};

unsigned char dotm_fontmap_lose[5][10] = {
	{0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x7F},	// L
	{0x1C, 0x22, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x22, 0x1C},	// O
	{0x3E, 0x41, 0x40, 0x40, 0x3E, 0x01, 0x01, 0x01, 0x41, 0x3E},	// S
	{0x7F, 0x40, 0x40, 0x40, 0x7F, 0x40, 0x40, 0x40, 0x40, 0x7F},	// E
	{0x1C, 0x22, 0x41, 0x55, 0x41, 0x49, 0x55, 0x41, 0x22, 0x1C}	// :(
};

unsigned char dotm_fontmap_fire[2][10] = {
	{0x08, 0x09, 0x55, 0x53, 0x61, 0x4D, 0x53, 0x53, 0x22, 0x1C},	// fire 1
	{0x08, 0x48, 0x55, 0x65, 0x43, 0x59, 0x65, 0x65, 0x22, 0x1C}	// fire 2
};

unsigned char dotm_fontmap_go[3][10] = {
	{0x1C, 0x22, 0x41, 0x40, 0x40, 0x4E, 0x41, 0x41, 0x21, 0x1E},	// G
	{0x1C, 0x22, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x22, 0x1C},	// O
	{0x1E, 0x21, 0x21, 0x39, 0x45, 0x59, 0x41, 0x41, 0x21, 0x1E}	// fist	
};

unsigned char dotm_fontmap_fight[5][10] = {
	{0x7F, 0x40, 0x40, 0x40, 0x7F, 0x40, 0x40, 0x40, 0x40, 0x40},	// F
	{0x7F, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x7F},	// I
	{0x1C, 0x22, 0x41, 0x40, 0x40, 0x4E, 0x41, 0x41, 0x21, 0x1E},	// G
	{0x41, 0x41, 0x41, 0x41, 0x7F, 0x41, 0x41, 0x41, 0x41, 0x41},	// H
	{0x7F, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08}	// T
};


int dotm_open(struct inode *pinode, struct file *pfile)
{
	int i;
	unsigned short wordvalue;
	
	/* When dotm_in_use is being used, terminate this function */
	if (dotm_in_use != 0) {
		return -EBUSY;
	}

	/* Set dotm_in_use as being used */
	dotm_in_use = 1;


	/* Set clear the dotmatrix  */
	for (i = 0; i < 10; i++) {
		wordvalue = dotm_fontmap_empty[i] & 0x7F;
                iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
        }

	return 0;
}


int dotm_close(struct inode *pinode, struct file *pfile)
{
	/* Set dotm_in_use as not being used */
	dotm_in_use = 0;

	return 0;
}


ssize_t dotm_write(struct file *pinode, const char *gdata, size_t len, loff_t *off_what)
{
	int ret, i;

	unsigned char num;
	unsigned short wordvalue;
	const char *tmp = NULL;

	tmp = gdata;

	if (len > 1) {
		printk(KERN_WARNING "only 1 byte data will be processed\n");
	}
	
	ret = copy_from_user(&num, tmp, 1);
	if (ret < 0) {
	    return -EFAULT;
	}

	/* Display the first byte on dotmatrix */
	for (i = 0; i < 10; i++) {
    		wordvalue = dotm_fontmap_decimal[num][i] & 0x7F;
    		iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
	}

	return len;
}


static long dotm_ioctl(struct file *pinode, unsigned int cmd, unsigned long data) {
    int i, j;
    unsigned short wordvalue;

    /* Check if the command has right magic number */
    if(_IOC_TYPE(cmd) != DOTM_MAGIC)
    {
    	printk("magic err\n");
	return -EINVAL;
    }

    switch(cmd) {
    	    case DOTM_SET_ALL:	// Set all the dots turned on
		    printk(KERN_DEBUG "DOTM_SET_ALL has been called\n");
		    for (i = 0; i < 10; i++) {
		    	wordvalue = dotm_fontmap_full[i] & 0x7F;
			iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
		    }
		    break;
	    
	    case DOTM_SET_CLEAR:	// Set all the dots turned off
    		    printk(KERN_DEBUG "DOTM_SET_CLEAR has been called\n");
		    for (i = 0; i < 10; i++) {
		    	wordvalue = dotm_fontmap_empty[i] & 0x7F;
			iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
		    }
		    break;

	    case DOTM_WIN:		// Print some string for winning situation
		    printk(KERN_DEBUG "DOTM_WIN has been called\n");
		    for (j = 0; j < 4; j++) {
		    	for (i = 0; i < 10; i++) {
			    wordvalue = dotm_fontmap_win[j][i] & 0x7F;
			    iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
			}
			msleep(200);	// Let it sleep for 0.2 seconds between characters
		    }
	    	    break;	    

	    case DOTM_LOSE:		// Print some string for losing situation
		    printk(KERN_DEBUG "DOTM_LOSE has been called\n");
		    for (j = 0; j < 5; j++) {
		    	for (i = 0; i < 10; i++) {
			    wordvalue = dotm_fontmap_lose[j][i] & 0x7F;
			    iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
			}
			msleep(200);	// Let it sleep for 0.2 seconds between characters
		    }
		    break;

	    case DOTM_SCORE:		// Print some string for scoring situation
		    printk(KERN_DEBUG "DOTM_SCORE has been called\n");
		    for (j = 0; j < 3; j++) {
		    	for (i = 0; i < 10; i++) {
			    wordvalue = dotm_fontmap_go[j][i] & 0x7F;
			    iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
			}
			msleep(200);
		    }
		    break;
		
	    case DOTM_BEAT:		// Print some string for beating situation
		    printk(KERN_DEBUG "DOTM_BEAT has been called\n");
		    for (j = 0; j < 5; j++) {
		    	for (i = 0; i < 10; i++) {
			    wordvalue = dotm_fontmap_fight[j][i] & 0x7F;
			    iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
			}
			msleep(200);
		    }
		    break;

	    case DOTM_FIRE:
		    printk(KERN_DEBUG "DOTM_FIRE has been called\n");
		    for (j = 0; j < 2; j++) {
		    	for (i = 0; i < 10; i++) {
			    wordvalue = dotm_fontmap_fire[j][i] & 0x7F;
			    iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
			}
			msleep(200);
		    }
		    break;

	    default:
    		    printk(KERN_WARNING "unsupported command %d\n", cmd);
    }
    
    return 0;
}

static struct file_operations dotm_fops = {
	.owner	= THIS_MODULE,
	.open	= dotm_open,
	.write	= dotm_write,
	.unlocked_ioctl = dotm_ioctl,
	.release= dotm_close,
};

static struct miscdevice dotm_driver = {
	.fops	= &dotm_fops,
	.name	= DOTM_NAME,
	.minor	= MISC_DYNAMIC_MINOR,
};

int dotm_init(void)
{
	misc_register(&dotm_driver);
	printk(KERN_INFO "driver: %s driver init\n", DOTM_NAME);

	return 0;
}

void dotm_exit(void)
{
	misc_deregister(&dotm_driver);
	printk(KERN_INFO "driver: %s driver exit\n", DOTM_NAME);
}

module_init(dotm_init);
module_exit(dotm_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");
