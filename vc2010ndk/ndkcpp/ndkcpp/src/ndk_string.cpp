#include <stdio.h>
#include <string.h>
#include <iostream>
#include "ndk_string.h"
/**
�ַ���������
*/
int test_str_patch()
{
	char a[]="abc abd wh baf wanh afb"; 
	char b[]="wa";
	char *pa = a,*pb = b;
	int local=1;
	while(*pa!='\0')
	{
		if(*pa == ' ')
			local++;
		if(*pb == *pa)
		{
			while(*pb == *pa)
			{
				pb++;
				pa++;
			}
  		   if(*pb == '\0')
			   break;
		   else  
			   pb = b;
		}
		pa++;
	}
	printf("%d\n",local);
	return 1;
}

/************************************************************
**
**function		��Ծ�����ַ���
**return		���ز��ҵ���λ���ִ�
*************************************************************/
int ndk_string_jump_match_test(){
	char a[]="lji";
	char b[3][5]={{"li"},{"chua"},{"jian"}};
	char c[3][5];
	int i = 0;
	int j_index = 0 ;
	int k = 0;
	int j = j_index;
	int match_c = 0;
	
	int j_max = strlen(b[j_index]);
	int c_num = 0;
	printf("ndk_string_jump_match_test���");
	for(; c_num < sizeof(c[3][5]); c_num++)
	{
		memset(c_num+c,0,sizeof(c));
	}

	for(j_index= 0; j_index < strlen(b[j_index]); j_index++)
	{
		j = j_index;

		for(;i < strlen(a) && k < strlen(b[j]);)
		{

			if(a[i] != b[j][k])
			{
				j++;
				k=0;
			}
			else
			{
				c[j][k] = 1;
				match_c++;
				i++;k++;
			}
			
			if(match_c == strlen(a))
			{
				//return c[3][5];
				int i_c;
				int j_c;
				printf("���Ϊ:");
				for(i_c = 0; i_c < 3; i_c++)
				{
					for(j_c = 0; j_c < 5; j_c++)
					{
						printf("%d",c[i_c][j_c]);
					}
				}
				printf("\n");

				return 1;
			}
		}
		memset(&c[3][5],0,sizeof(c[3][5]));
		match_c = 0;
		//j_max = strlen(b[j_index]);
	}
	return 0;
}


/************************************************************
**
**function		����ƥ���ַ���
**return		��һ��ƥ���ִ�λ��
*************************************************************/
int ndk_string_match()
{
	char a[]="abc abd wh baf wanh afb"; 
	char b[]="wa";
	char *pa = a,*pb = b;
	int local=1;
	while(*pa!='\0')
	{
		if(*pa == ' ')
			local++;
		if(*pb == *pa)
		{
			while(*pb == *pa)
			{
				pb++;
				pa++;
			}
  		   if(*pb == '\0')
			   break;
		   else  
			   pb = b;
		}
		pa++;
	}
	printf("%d\n",local);
	return 1;

}
