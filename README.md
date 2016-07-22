# MVVMDemo

![](http://upload-images.jianshu.io/upload_images/1603789-a7f7676041d1e59b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)android MVVM开发模式

概念
**mvvm** 是一个在 **mvp** 架构上修改，目标是将view的一些更改，跟model关联起来，使得model的数据改变，直接通知到view上面来，从而解决**mvp**架构里面的v-p之间的接口太重问题。
所以mvvm的核心解决问题为：使得v-p直接的关系弱化，使用绑定方式(dataBinding)直接将model的改变反馈到view上面。
关于完整的dataBinding讲解，请看这里
https://github.com/LyndonChin/MasteringAndroidDataBinding
本讲解决什么问题呢？一个方面是简单的使用dataBinding方式，一个是讲解它内部如何做到的。
mvvm的含义：
mvvm指的是model view 和viewmodel。
model 就是数据实现和逻辑处理。
view 就是界面显示。
viewmodel 创建的关联，将model和view绑定起来。如此之后，我们model的更改，通过viewmodel反馈给view。（view的xml布局文件，经过特定的编写，编译工具处理后，生成的代码会接收viewmodel的数据通知消息，自动刷新界面）

![](http://upload-images.jianshu.io/upload_images/1603789-5edd660508e63af7?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

说多无益，直接看例子：
1在项目的build.gradle

![](http://upload-images.jianshu.io/upload_images/1603789-c25e736cfefdd283?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2在模块的build.gradle

![](http://upload-images.jianshu.io/upload_images/1603789-bba971c5923a8749?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

加入dataBinding

3创建一个User类

![](http://upload-images.jianshu.io/upload_images/1603789-8f79ed7d84ff0061?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

里面有一个属性name，我们等会要用。
4 编写Activity_main.xml布局文件

![](http://upload-images.jianshu.io/upload_images/1603789-0a08cadf8255935a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

布局根节点换成layout
加入data节点
写入一个变量，名字user，类型为com.xm.mvvmdemo.Model.User，也就是3这个类的位置。
在TextView的属性android:text上写入@{user.name}
这里这个动作，在编译后代码会生成类似：textview.setText(user.getName());实际有些特殊，后面再说。
5在MainActivity里面修改

![](http://upload-images.jianshu.io/upload_images/1603789-adf37c611bd740d3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

替换setContentView（R.layout.activity_main）换成DataBindingUtil.setContentView(this, R.layout.activity_main)，返回的是生成的绑定类ActivityMainBinding（这个就是我们的xml名字activity_main去掉连接，首字母大写，拼接一个Binding组成的。）
这里更换成这个DataBindingUtil.setContentView，内部做了哪些动作呢？在默认的setContentView动作后，做了一个绑定动作，将里面的view和数据关联起来。具体看怎么关联的，可以看ActivityMainBinding这个生成出来的类。

创建一个User，设置值。然后绑定。
通过binding.setUser()，将我们的数据传回给view,通过我们自动生成的代码，可以直接将这个值赋值过去。

现在我们捋一捋这个思路。
改变xml，编译之后，出来一个真正布局xml和一个binding代码(这里是ActivityMainBinding.java)，这里ActivityMainBinding里面就是生成出来的将xml里面的绑定地方的描述改为代码：（这里android:text:”@{user.name}”）生成为![](http://upload-images.jianshu.io/upload_images/1603789-4529bfa812eabd40?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
第一个参数便是我们的TextView类，第二个值我们看到是从上面的User的实例出getName的值。
第一个参数如何来的：

![](http://upload-images.jianshu.io/upload_images/1603789-b2c6f60b4f2ef8fc?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

改变setContentView单一动作，换为DataBindingUtil.setContentView。上面已经提过这个的思路。

现在我们写好了绑定xml，替换了setContentView，这两个动作完成后，我们的绑定类已经把里面的view和需要的数据绑定起来了，这时我们是不是需要给赋值呢？

binding.setUser(user);将数据赋值过去，这样子里面则就将信息反馈给界面了。

6总结
其实使用的时候，第一印象是我xml都能写哪些东西啊，这个是我的第一个问题。
我们AS选择到Project模式下，拉到最下方的地方，看到External Libraries ，可以看到一个adapters-1.1.jar，打开就看到所有的可用属性了。

我要自己定义属性，系统的默认不够用？
好吧，自己去写标记为@BindingAdapter的一个静态方法就可以了。随意放，编译器会自己找这个注释的。

注意：绑定的编译处理过程我们不管，我们把它当做黑盒子处理，看下它处理我们的布局xml的前后对比。

![](http://upload-images.jianshu.io/upload_images/1603789-650fdb32c0e773b6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里我们看到在data节点标记了名字和类型，然后底下的去用这个名字里面的属性。我们发现，这里我们只需要约定下这个关联方法，这个就完全可以自动生成了。
因此，我们默认属性的获取都是通过get得到。这里user.name就变成uset.getName（） 关于这个android:text属性呢，默认就是对应的set方法了，这里没有使用默认的，why？因为为了严谨，需要做赋值过滤。系统默认提供了具体方法：
![](http://upload-images.jianshu.io/upload_images/1603789-6a448d3bcc13c76c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们看下这个布局和生成的布局之间的对比：
![](http://upload-images.jianshu.io/upload_images/1603789-fe6a432268c9456a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

生成的时候直接将上面的layout和data拿掉了。将android:text也去掉了，这些去哪里呢？（答案就是这个布局xml生成一个真正的布局文件和一个管理绑定的代码啦，这里就是ActivityMainBinding）
 ActivityMainBinding 主要完成几件事情1：将里面的需要绑定的view实例拿到。2：将view和model的关联代码生成出来。（也就是model该如何显示到view上）
说了这么多，再一次总结下databinding做了什么：
依据约定的xml（layout作为根节点），生成真正的xml和一个**绑定类**，**绑定类**为我们做了界面里面的View和数据之间更新的规则。
然后我们用DataBindingUtil.setContentView 替换setContentView，因为它在setContentView的基础上做了一个动作，就是把这个界面里面的View和数据之间更新的规则直接交给我们的**绑定类**。
如此一来，我们发现我们要写的findviewbyid 和setText 被这个工具自动生成出来了，我们将这些动作写入了布局里面，让工具帮我们做了代码生成，从而让我们解放了繁琐重复的界面操作。
好吧，先这么多，这个架构要写的太多，敬请期待。

![](http://upload-images.jianshu.io/upload_images/1603789-554d3744459f610b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
