# MVVMDemo

![](http://upload-images.jianshu.io/upload_images/1603789-a7f7676041d1e59b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

android MVVM开发模式

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
改变xml，编译之后，出来一个真正布局xml和一个binding代码(这里是ActivityMainBinding.java)，这里ActivityMainBinding里面就是生成出来的将xml里面的绑定地方的描述改为代码：（这里android:text:”@{user.name}”）生成为
![](http://upload-images.jianshu.io/upload_images/1603789-4529bfa812eabd40?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

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

简介
之前讲了那么多，主要围绕着这个核心概念:**绑定**。
不过相对来说，我们上节讲的其实只是它的一个知识点:解决布局文件的生成和界面的绑定。数据是我们主动给到绑定类的，这个肯定不是我们的目标。主动给到绑定的动作应该只是第一次作为初始值给出，随后则需要数据直接改变，自动反馈到界面上。
所以，我们这节就来解决这个问题。
还是老样子，先看如何使用它：
1让User继承BaseObservable
![](http://upload-images.jianshu.io/upload_images/1603789-48c38b626adb2c1a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

BaseObservable叫可观测，也可以简单的理解成我们常说的callback回调函数，就像点击一个button注册的onclick一样，一点击，就调用回调函数。

BaseObservable是个有通知机制，我们可以借助这个来完成数据改变的反馈。

2使用@Bindable注释

![](http://upload-images.jianshu.io/upload_images/1603789-bf181c783a143dfc?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

@Bindable注释的目的是使得编译机制知道，这个类里面有个数据是需要通知的，因此它会生成通知和被通知的关系链。

这样说肯定会晕，因此我们说下这里的name，我们记得我们的xml里面写过

![](http://upload-images.jianshu.io/upload_images/1603789-765c3e61ad91c54d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

意思就是文本是需要这个name的值来显示的。我们给User的String name加上这个注释@Bindable，就是告诉编译器你给我把这两个关联起来。（这里的关联指的是在初始化设置User的时候，

![](http://upload-images.jianshu.io/upload_images/1603789-08f7ba5bb9e11419?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

将自己（ActivityMainBinding）作为User的回调，这样子我们有变化则可以给通知了）
3通知数据改变

![](http://upload-images.jianshu.io/upload_images/1603789-735103677e746742?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在我们的User里面，写上

关键技术点：notifyPropertyChanged(BR.name);

通知属性改变了，这里BR类是编译生成的，主要就是@Bindable标记的属性，生成这个的目的就是为了我们去通知数据变化时候使用。

4实践

![](http://upload-images.jianshu.io/upload_images/1603789-9ccba4e6aac8ca71?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里和上一节讲的区别在哪呢？我们看到先设置User，然后才给name赋值的，设置User过后，这时候再去设置name，然后通过notifyPropertyChanged通知数据改变，而上一节说过，ActivityMainBinding的目的就是将数据和view绑定起来，因此现在我们数据变化了，主动通知我们的绑定模块（ActivityMainBinding），从而实现数据变化直接反馈到界面了。这样子我们只需要关注数据的变化即可了。

5 代码地址
https://github.com/luxiaoming/MVVMDemo
6后记
说了mvvm的开发模式思路，我们发现它的优势是将mvp中的v-p关系简化，但是它的做法是将这个动作做到了xml里面去了，这时就看你的取舍了。喜欢写入xml，就用这个思路，其实实际中mvvm有个和mvp的折中思路，就是mvp是v-p关系太过紧密，mvvm的缺点是没有了Presenter，使得vm（ViewModel做了很多Presenter的事情）太重，因此有人就搞出来两个的优点合并，在mvp的基础上，使用ViewModel，两个混合着用，可以达到两者的优点共存，google官方例子也是如此推荐使用的。

常用的开发模式 mvp+dagger2 或者就是mvvm+dagger2。

更多细节，可以看官网原文：
https://developer.android.com/topic/libraries/data-binding/index.html
简书中文翻译：
http://www.jianshu.com/p/b1df61a4df77
完全demo：
https://github.com/LyndonChin/MasteringAndroidDataBinding

为了更好地理解@BindingAdapter，github地址里面提交了一个adapters目录，将系统提供的适配提交上去，以便查阅。

回顾

我们前两节讲了哪些内容呢？
mvvm模型概念
dataBinding是什么
演示xml如何变为了代码
演示了数据自动通知BaseObservable

通过上面的学习，我们掌握了xml变成代码，并且数据（Model）和View通过dataBinding关联起来是如何操作的了。有人就说了，毕竟系统提供的适配器（@BindingAdapter标注的静态方法 ）有限，我们能自己定义属性对应的方法吗？
答案是YES。因此我们就要自己去写自己的@BindingAdapter，来完成我们所需要的功能。如此一来，我们会发现这个dataBinding的突出优势来了。
so，我们看步骤：
1加个自定义属性

![](http://upload-images.jianshu.io/upload_images/1603789-e8577fe51d13391a?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

添加一个age年龄的属性，标记类型为整数

2在我们的xml使用

![](http://upload-images.jianshu.io/upload_images/1603789-492cd6fabfe1bef3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

加入属性值，和User里面的age关联。（因为是我们自定义的属性，系统肯定不知道对应的函数方法是什么，因此@BindingAdapter来给指明方向）

3适配方法

![](http://upload-images.jianshu.io/upload_images/1603789-b51398d4967a41db?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

随便在一个位置，写入如下代码：必须静态函数，原因很好理解。（不是静态含需要对应实例，用起来很怪。）
两个参数，第一个就是我们是在哪里是用的，因为我们是在一个TextView使用的，因此第一个参数是TextView类型。第二个就是我们定义的类型了，int，实现下操作即可。

4演示
在我们的User类里面，写上：

加入了一个值age，标记成可观测

![](http://upload-images.jianshu.io/upload_images/1603789-4dd3d1c7b7bff989?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
实现一个设置接口啦。

![](http://upload-images.jianshu.io/upload_images/1603789-ba04fcf9e4ad7ae0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

调用地方：

![](http://upload-images.jianshu.io/upload_images/1603789-98c737839c57191e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们看到，当赋值年龄之后，界面正确的显示出来了。效果如下：

![](http://upload-images.jianshu.io/upload_images/1603789-919daadd881d90b2?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

5后记
我们看下这个@BindingAdapter最后在ActivityMainBinding承载着什么关系，直接看代码：

![](http://upload-images.jianshu.io/upload_images/1603789-9acd02611cfd4577?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

看到了吧。我们使用工具的时候，第一要素是什么呢？就是我们先去思考我们会遇见哪些问题，然后就可以去找对应的解决方案了。用我们的实际做法，去对应着这个dataBinding来实现，这样子我们就很快理解这个东西的实质了。

6代码位置
https://github.com/luxiaoming/MVVMDemo
![](http://upload-images.jianshu.io/upload_images/1603789-06ca8c03230bf5c3?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
android MVVM开发模式（四）
上节我们讲了自定义的@BindingAdapter，来扩展属性功能的时候，第一步添加了一个自定义属性这个其实是多余的。（我当时按照自定义view属性去做了，其实data-binding是不用这个的，它的实现原理是找到标记为@BindingAdapter对应属性之后，依据这个函数生成代码即可，这个属性在真正的xml里面，是会去掉的。）
这个是怎么发现的呢？
在继续思考@BindingAdapter的定义时候发现的。因为我们标记的时候，后面的参数可能是任意结构的，而本身属性里面标记的类型是有限的，从这里发现这个问题的。
小插曲 说完了。
我们上一节说了怎么玩@BindingAdapter，我们再来说下它是做什么的
通过标记一个静态方法为@BindingAdapter，标记附加值为对应属性。

静态方法参数：第一个为view的类型，随后参数就是我们关联的变量类型。

目的就是可以自定义任意形式的属性适配器。

我们之前还讲了BaseObservable类 和 @Bindable
使用类继承BaseObservable后，然后在属性的前面标记为@Bindable，这个属性则是可以通知的。通过使用 notifyPropertyChanged(BR.age);向界面通知。 参数就是这个属性对应的值。

回顾完成，我们看到了这个现在做的是数据更改，通知给view，没有一个view上面输入数据后，反馈给数据这边。因此我们这节解决这个问题。
我们一起看步骤：
1 setAge函数
![](http://upload-images.jianshu.io/upload_images/1603789-b54210d0d8ceca0c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里我们多了一个条件判断，判断如果没有变化，停止设置text，原因是如果不设置，因为设置text会引起文本改变回调，回调回来又设置文本，又引起文本改变，继续回调，造成无限循环。

2 再加一个标记适配器
![](http://upload-images.jianshu.io/upload_images/1603789-60762137a21f3523?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
参数注意，第二个参数要求是这个InverseBindingListener，原因是我们作为双向通讯，这个作为桥梁。这里我们使用检测文本改变，然后调用 ageAttrChanged.onChange();即可。

3 牵线搭桥
![](http://upload-images.jianshu.io/upload_images/1603789-0c0ee6a6564dfeb8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
关键的标注来了。@InverseBindingAdapter，两个参数，属性 和事件。事件后面的值和上面2里面的标注适配器值一样。

这里我们停一下，思考下，两个适配器![](http://upload-images.jianshu.io/upload_images/1603789-5ef5d7a775de1c3d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)![](http://upload-images.jianshu.io/upload_images/1603789-b6a5bb0c1c230a41?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)和一个关联![](http://upload-images.jianshu.io/upload_images/1603789-850b4adacca173b5?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)它的逻辑思路是：
适配器ageAttrChanged 来完成TextView的注册文本改变消息处理。里面使用onChange()调用。
关联的来处理onChange（）的内部实现，返回值就是你的变量类型。
4 临门一脚
![](http://upload-images.jianshu.io/upload_images/1603789-9d92889624028a5e?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)改变@ 为 @= ，变为双向方式
如此一来，达到view的数据变化，传递给数据这边。我们之前讲过如何将数据通知给view。这两个组合起来，则完成了双向通讯。
5 验证
![](http://upload-images.jianshu.io/upload_images/1603789-eec82f764f86795b?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里先设置为28，然后在post里面做文本变更，通知到数据那边，然后数据那边设置一下，反馈给界面验证。

这里为什么用post呢？原因是binding内部处理数据是个异步的，所以当前这个消息队列里面，如果我们修改文本，因为文本改变回调还没注册呢，导致数据那边没同步了。（当然实际使用中这个情况很少的啦。）

6代码
https://github.com/luxiaoming/MVVMDemo

android MVVM开发模式（五）
上一讲我们说了@InverseBindingAdapter标记的事情。通过这个，我们可以实现view向数据方向的传递。从而实现真正的双向绑定。
已经讲了这么多，核心的知识点基本完成，要每个细节去说，估计十几节讲不完了，因此我们再来看下一个核心内容，结束本系列教程。如果使用中有任何疑问，欢迎沟通交流。
我们前面讲过，@BindingAdapter 实现的函数必须是静态的。那它不能是非静态的吗？答案是可以的。
这个需求的出现是因为 我们有时会想在两种情况下，一个属性出现两种表现逻辑代码，这个主要的需求便是 正常流程 和测试模式，测试下可以模拟一条线路，不必走我们正常流程下的数据，可以直接提供测试数据，来测试代码。
当然，如果我们使用一个属性，来改变界面颜色搭配。那么实现两个同一属性的方法，配置不同的颜色，通过设置使用哪个代码，可以达到换肤的效果啦。
我们看下怎么操作的。
移除掉User里面的@BindingAdapter @InverseBindingAdapter 的方法，原因是我们要给这个属性使用两组实现。

1 实现一个抽象类。
![](http://upload-images.jianshu.io/upload_images/1603789-62630c73aceb12ba?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里有个问题，本来按照我们的理解，@InverseBindingAdapter注解的函数方法，应该也是可以非静态的（我们想使用测试时候反馈和正常反馈有些差异，然而当前data-binding不允许这个为非静态~~~~。这个我看了生成代码，是完全可以做到为非静态的，所以不解为什么系统当前不支持。）

我们定义了两个抽象的属性适配，没有实现代码。

2 我们实现它
![](http://upload-images.jianshu.io/upload_images/1603789-ef73020a427f33b8?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里继承它，实现两个抽象方法。

3 实现DataBindingComponent接口
![](http://upload-images.jianshu.io/upload_images/1603789-0d09a5a327a0865c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
在我们做了上面的动作，具体指的是@BindingAdapter实现的方法是非静态的时候，那么我们肯定需要一个对应实例的吧。因此data-binding帮你将你需要写的方法生成了一个接口，让你去实现它就行了。（很智能化吧。）

我们实现它，返回一个适配器就可以了。在这里我们就可以清晰地看到，这个返回的我们可以定制的。如果我们多个继承了BaseAdapter类，比如OptionAdapter 和OptionAdapter2 ，我们这里可以再写一个组件，返回OptionAdapter2，在我们的代码里面依据情况使用不同的组件。

4 使用它
![](http://upload-images.jianshu.io/upload_images/1603789-940c0e8477734a6c?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里关键点，最后一个参数。将我们的组件实例传递给它。这样子生成的绑定里面，才能依据这个找到真正实现属性对应方法的实例，然后调用。

我们可以看到绑定的实现里面，做了这个动作。![](http://upload-images.jianshu.io/upload_images/1603789-e195a685fc3b5f8d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

mBindingComponent 是我们传递进来的，这里使用它的getBaseAdapter后再调用方法了。 所以我们通过传递不同的BindingComponent ，来实现同一属性多个实现方法了。

5 代码地址
https://github.com/luxiaoming/MVVMDemo
![](http://upload-images.jianshu.io/upload_images/1603789-4c3affbc6acdca9d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
