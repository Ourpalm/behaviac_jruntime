# behaviac_jruntime

#### 介绍
腾讯行为树behaviac的java运行时

by ctemple@163.com

#### 注意

event && task中返回，在task中没有wait节点时，会嵌套执行父树，并吃掉父树的返回值EBTStatus),如果btexec用返回值来判断行为树是否结束，会有问题！
额外支持了在行为树执行中时，触发事件,可能导致循环多执行

