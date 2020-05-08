# behaviac_jruntime

#### 介绍
腾讯行为树behaviac的java运行时

by ctemple@163.com

#### 注意

1)event && task中返回，在task中没有wait节点时，会嵌套执行父树，并吃掉父树的返回值EBTStatus),如果btexec用返回值来判断行为树是否结束，会有问题！

2)运行中触发事件延后到下一帧, 此节点必须保证返回failure，打断父节点的执行!不然会继续执行后续节点。

