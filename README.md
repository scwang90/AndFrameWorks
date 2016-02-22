# 安卓AndFrame开发框架说明文档
## 一、框架简介
AndFrame 是一个能够让你快速进行Android开发的开源框架，它能让你专注于真正重要的地方。使代码更加精简，使项目更加容易维护。使用AndFrame框架之后，相比原生的Android开发能够让你少些很多代码。

AndFrame 框架不同于网上的大部分框架，大部分框架（如：xUtils）往往集成了各式各样的工具集供使用者使用，看起来更像是一个大型的工具包，少部分框架（如：androidannotations）实现了Android开发的依赖注入却没有丰富飞工具集。AndFrame正是继承了两者并加上异常处理控制的新一代开发框架，更像Spring框架一样入侵使用者的代码，一样的可以使用依赖注入，也会要求使用者继承AndFrame框架中的一些基类，如AfApplication（必须继承）和AfActivity（建议继承）等等。

AndFrame框架中公开的类（基类和工具类）都以Af作为类名前缀开头，代表了AndFrame的缩写，也防止类名和使用者的类名冲突。
## 二、功能列表
### 1.依赖注入
AndFrame框架提供了 @BindLayout、@BindView、@Inject、@InjectExtra 等等注解来实现以来注入，使使用者摆脱原生开发中那烦人的findViewById。
### 2.事件绑定
AndFrame框架提供了 @BindClick、@BindLongClick、@BindItemClick、@BindItemLongClick、@BindCheckChange 等等注解来实现控件事件的绑定。原生开发中需要先findViewById然后再setOnClickListener来实现事件绑定。现在一个BindClick就可以了。
### 3.常用封装
继承AfActivity之后可以使用 doShowDialog、doShowDialog、doShowViewDialog、doSelectItem、doInputText、showProgressDialog 来方便的弹出各种功能的对话框。也可以使用getSoftInputStatus、setSoftInputEnable、来简单的对软键盘进行控制。
### 4.常用类封装
AndFrame 提供了很多封装类，来封装原生开发中复杂繁琐的逻辑，如：AfActivity（通用页面）、AfListViewActivity（列表页面）、AfMainActivity（主页面）、AfListManageActivity（可管理列表页面）、AfMultiChoiceListActivity（可多选页面）、AfAlbumActivity（相册预览页面）、AfListAdapter（通用列表适配器）、AfExpandableAdapter（可折叠适配器）、AfImageCaches（图片缓存）、AfPrivateCaches（私有缓存）、AfDurableCache（持久缓存）
### 5.常用控件
AndFrame 提供了 AfAlbumView（相册查看控件）、AfDragLayout（抽屉布局）、AfRefreshListView（刷新分页列表控件）、AfTreeListView（树形列表控件）、AfTableRow（表格控件）、AfContactsListView（联系人列表控件）
### 6.图片下载与缓存
AndFrame提供了AfImageService 图片管理服务类来实现轻松下载图片和缓存。
### 7.文件下载器
AndFrame 提供了 AfDownloader 功能强大的文件下载器
### 8.简单异步任务 AfTask
AndFrame提供了 AfTask 、AfHandlerTask 等一系列的异步任务来实现异步请求和操作，摆脱了原生开发中handler会产生大量代码，并且不好维护的烦恼。
### 9.ORM框架 
AndFrame 提供更傻瓜异步增删改查工具类AfDao、AfEntityDao、AfDbOpenHelper，数据库在内置卡和外置卡都可以，解决了写sql、建表工作量大的问题。
### 10.常用组件
AndFrame 提供了许多组件如：AfModuleTitlebar（标题栏）、AfModuleProgress（加载中）、AfModuleNodata（空数据）、AfFrameSelector（帧选择组件）、AfSelectorTitlebar（可选择的标题栏）、AfSelectorBottombar（底部功能按钮栏）。
组件就是多个控件的集合功能提现，并且这些组件的布局样式都可以继承自定义。
### 11.异常处理
AndFrame 提供了AfExceptionHandler来对未知的异常进行处理，防止App崩溃闪退。默认情况下如果是调试模式，AfExceptionHandler会把捕捉到的异常直接在App页面以对话框的形式显示出来（发布模式不会），并且会把异常保持到文件中，如果绑定了邮件模块的话，还可以把捕捉到的异常发到指定的邮箱中。
### 12.常用工具包
AndFrame提供了大了工具类，如：AfDateFormat（时间格式化）、AfReflecter（Java反射）、AfStringUtil（字符串工具类）、AfMD5（加密工具）、AfFileUtil（文件处理）、AfCollections（集合处理）、AfExtraIntent（页面传参）、AfImageThumb（图片剪裁）、AfLocation（定位转换）、AfMeasure（屏幕单位转换）、AfZipUtil（压缩）、AfSIMCardInfo（SIM卡获取）、AfImageHelper（图片处理）、AfGifHelper（动态图片处理）、AfDesHelper（DES加密）、AfDeviceInfo（设备信息获取）等等。
