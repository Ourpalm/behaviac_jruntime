# behaviac_jruntime

#### 介绍
腾讯行为树behaviac的java运行时

by ctemple@163.com

#### 注意

event && task中返回，在task中没有wait节点时，会嵌套执行父树，并吃掉父树的返回值EBTStatus),如果btexec用返回值来判断行为树是否结束，会有问题！
额外支持了在行为树执行中时，触发事件,会嵌套执行，因为序列等节点还未设置status，exec时重新触发onenter，导致序列重新开始执行，无法回到旧节点!

