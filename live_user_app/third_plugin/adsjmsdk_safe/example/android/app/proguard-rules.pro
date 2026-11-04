# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.sjm.sjmsdk.core.config.SjmAdConfig {*;}
-keep class com.sjm.sjmsdk.core.db.SjmSdkDbManager {*;}
-keep class com.sjm.sjmsdk.core.DeviceId.SjmDeviceId {*;}
-keep class com.sjm.sjmsdk.dexmanager.SjmDexManager {*;}
-keep class com.sjm.sjmsdk.SjmSdk {*;}
-keep class com.sjm.sjmsdk.SjmSdk$SjmSdkInitListener {*;}
-keep class com.sjm.sjmsdk.core.Provider.SjmFileProvider {*;}
-keep class com.sjm.sjmsdk.ad.** { *; }

-keep class com.sjm.sjmsdk.core.config.SjmSdkConfig { *; }
-keep class zj.xuitls.* { *; }
-keep class com.sjm.sjmsdk.SjmSdkManager {*;}
-keep interface com.sjm.sjmsdk.SjmH5ContentListener {*;}
-keep class com.sjm.sjmsdk.SjmUser {*;}
-keep class com.sjm.sjmsdk.SjmGameActivity {*;}
-keep class com.sjm.sjmsdk.SjmGameSpaceActivity {*;}
-keep class com.sjm.sjmsdk.js.SjmJSSdk  {
                                        public <methods>;
}
-keep class com.sjm.sjmsdk.js.SjmJSAppSdk   {
                                                                               public <methods>;
                                       }
-keep class com.sjm.sjmsdk.js.SjmJSAdSdk   {
                                                                              public <methods>;
                                      }
-keep class com.bun.supplier.IIdentifierListener { *; }

#V1.3.0.0及以上版本
-ignorewarnings
-dontwarn com.lechuan.midunovel.**
-keep class com.lechuan.midunovel.** { *; }

-keep class com.qq.e.** { *; }
-keep interface com.qq.e.** { *; }
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}
#
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}


-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-keep class com.kwad.**{ *; }
-keep class com.kwad.sdk.** { *;}
-keep class com.ksad.download.** { *;}
-keep class com.kwai.filedownloader.** { *;}
-keepclasseswithmembernames class * {
native <methods>;
}
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**

#oaid
-dontwarn com.bun.**
-keep class com.bun.** {*;}
-keep class a.**{*;}
-keep class com.netease.nis.sdkwrapper.Utils {*;}
-keep class com.bun.supplier.IIdentifierListener { *; }
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}


-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep interface androidx.** { *; }
-keep class androidx.** { *; }
-keep public class * extends androidx.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }

-keep class mobi.oneway.** {*;}

#优化  不优化输入的类文件
-dontoptimize

# 枚举类不能被混淆
-keepclassmembers enum * {
<fields>;
public static **[] values();
public static ** valueOf(java.lang.String);
}


-keep class sun.misc.Unsafe { *; }

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontwarn com.sigmob.**.**
-keep class com.sigmob.**.**{*;}
-keep interface com.sigmob.**.** {*;}
-keep enum com.sigmob.**.** {*;}


-keep class com.bytedance.sdk.openadsdk.** { *; }
#-keep class com.bytedance.sdk.component.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep public class com.ss.android.download.api.**{*;}
-keep public class com.ss.android.downloadad.api.**{*;}
-keep class com.ss.android.socialbase.downloader.depend.IDownloadFileUriProvider{*;}
-dontwarn com.tencent.smtt.sdk.WebView
-dontwarn com.tencent.smtt.sdk.WebChromeClient

-dontwarn androidx.annotation.Nullable
-dontwarn androidx.annotation.NonNull
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient
-dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient$Info
-dontwarn androidx.appcompat.app.AlertDialog
-dontwarn androidx.appcompat.view.menu.ListMenuItemView
-dontwarn androidx.recyclerview.widget.RecyclerView
-dontwarn androidx.swiperefreshlayout.widget.SwipeRefreshLayout
-dontwarn androidx.viewpager.widget.ViewPager
-dontwarn androidx.recyclerview.widget.RecyclerView
-dontwarn androidx.annotation.RequiresApi
-dontwarn androidx.fragment.app.FragmentActivity
-dontwarn androidx.fragment.app.Fragment
-dontwarn androidx.annotation.AnyThread
-dontwarn androidx.annotation.WorkerThread

-keep class com.bytedance.embedapplog.AppLog { public *; }
-keep public interface com.bytedance.embedapplog.IDataObserver { *; }
-keep public interface com.bytedance.embedapplog.IAppParam { *; }
-keep public interface com.bytedance.embedapplog.IExtraParams { *; }
-keep public interface com.bytedance.embedapplog.IPicker { *; }
-keep public interface com.bytedance.embedapplog.IOaidObserver { *; }
-keep public interface com.bytedance.embedapplog.ISensitiveInfoProvider { *; }
-keep class com.bytedance.embedapplog.IOaidObserver$Oaid { *; }
-keep class com.bytedance.embedapplog.GameReportHelper { public *; }
-keep class com.bytedance.embedapplog.InitConfig { public *; }
-keep class com.bytedance.embedapplog.util.UriConfig { public *; }
-keep class com.bytedance.embedapplog.tracker.Tracker { public *; }
-keep class com.bytedance.embedapplog.picker.Picker { public *; }
-keep class com.bytedance.embedapplog.tracker.WebViewJsUtil { *; }
-keep interface com.bytedance.embed_dr.DrLogWriter { public *; }
-keep interface com.bytedance.embed_bdtracker.bt { public *; }
-keep class com.bytedance.embed_bdtracker.bt$a { public *; }
-keep class com.bytedance.embed_bdtracker.bt$a$a { public *; }
-keep class com.bytedance.embed_dr.VivoIdentifier {*;}
-keep class com.bytedance.embed_dr.VivoIdentifier$1 {*;}

-keep class ms.bd.c.**

-keep class com.bytedance.mobsec.metasec.ml.MS {
    *;
}
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.bytedance.frameworks.** { *; }

-keep class ms.bd.c.Pgl.**{*;}
-keep class com.bytedance.mobsec.metasec.ml.**{*;}

-keep class com.ss.android.**{*;}

-keep class com.bytedance.embedapplog.** {*;}
-keep class com.bytedance.embed_dr.** {*;}

-keep class com.bykv.vk.** {*;}

-keep interface com.bytedance.frameworks.baselib.network.http.cronet.I* {*;}
-keepnames class com.bytedance.framwork.core.sdkmonitor.SDKMonitorUtils



#获取OAID的SDK
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
#基础模块
-keep class com.youxiao.ssp.base.listener.* {
*;
}
-keep class com.youxiao.ssp.base.bean.ShareData {
public <methods>;
}
-keepclassmembers class * extends com.youxiao.ssp.base.core.JsBridge {
public <methods>;
}
-keep class com.youxiao.ssp.ad.core.AdClient {
public <methods>;
}
-keep class com.youxiao.ssp.ad.listener.* {
*;
}
-keep class com.youxiao.ssp.ad.bean.SSPAd {
public <methods>;
}
-keep class com.youxiao.ssp.ad.bean.NextAdInfo {
public *;
}
-keep class com.youxiao.ssp.ad.bean.SSPContentItem {
*** get*();
}
-keep class com.youxiao.ssp.coupon.listener.* {
*;
}
-keep class com.youxiao.ssp.base.activity.SSPWebActivity {
public <methods>;
}
-keep class * extends com.youxiao.ssp.base.activity.SSPBaseActivity {
public static <fields>;
public static <methods>;
}
-keep class * extends com.youxiao.ssp.base.activity.SSPBaseFragmentActivity {
public static <fields>;
public static <methods>;
}
-keep class * extends android.support.v4.app.Fragment {
public static <fields>;
public static <methods>;
}
-keep class com.youxiao.ssp.core.SSPSdk {
public <methods>;
}
#广告模块（*** 纯电商优惠券SDK无需添加广告混淆配置 ***）
-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }
-keep class com.qq.e.** {
public protected *;
}
-keep class android.support.v4.**{
public *;
}
-keep class android.support.v7.**{
public *;
}
-keep class yaq.gdtadv{
*;
}
-keep class com.qq.e.** {
*;
}
-keep class com.tencent.** {
*;
}
-keep class cn.mmachina.JniClient {
*;
}
-keep class c.t.m.li.tsa.** {
*;
}
-keep class * extends java.lang.annotation.Annotation { *; }
-keep interface * extends java.lang.annotation.Annotation { *; }
-keep, allowobfuscation class com.qq.e.comm.plugin.services.SDKServerService
{*;}
-keepclassmembers, allowobfuscation class
com.qq.e.comm.plugin.net.SecurePackager {
public *;
}
-keepclasseswithmembers,includedescriptorclasses class * {
native <methods>;
}
-keep class * extends com.qq.e.mediation.interfaces.BaseNativeUnifiedAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseSplashAd { *; }
-keep class * extends com.qq.e.mediation.interfaces.BaseRewardAd { *; }
-keep class com.bykv.vk.** { *; }
-keep class ms.bd.c.Pgl.**{*;}
-keep class com.bytedance.** { *; }
-keep class com.pgl.** { *; }
-keep class com.ss.android.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-keep class com.kwad.**{ *; }
-keep class com.kuaishou.**{ *; }
-keepclasseswithmembernames class * {
native <methods>;
}
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**
-keep class com.yilan.sdk.**{
*;
}
-dontwarn javax.annotation.**
-dontwarn sun.misc.Unsafe
-dontwarn org.conscrypt.*
-dontwarn okio.**
-keep class com.fun.** {*;}
-keep class com.funshion.** {*;}
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {
@android.support.annotation.Keep <init>(...);
@android.support.annotation.Keep <methods>;
@android.support.annotation.Keep <fields>;
}
-keep class okhttp3.** {*;}
-keep class okio. {*;}
-keep class com.alibaba.fastjson.** {*;}
-keep class sun.misc.Unsafe { *; }
-dontwarn com.sigmob.**
-keep class com.sigmob.**.**{*;}
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }
#厂商广告平台（*** 未接入厂商广告平台或纯电商优惠券SDK无需添加广告混淆配置 ***）
-keep class com.miui.** { *;}
-keep class com.xiaomi.** { *; }
-keep class * extends android.os.IInterface{ *; }
-keep class com.huawei.openalliance.ad.** { *; }
-keep class com.huawei.hms.ads.** { *; }
-keepattributes SourceFile,LineNumberTable
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-keep class com.vivo.*.** { *; }
-dontwarn com.bytedance.article.common.nativecrash.NativeCrashInit
-keep class com.androidquery.callback.** {*;}
-keep class com.ss.sys.ces.* {*;}
-keep class com.opos.** { *;}
-keep class com.heytap.msp.mobad.api.** {*;}
-keep class com.heytap.openid.** {*;}
#电商模块（*** 纯广告SDK无需添加电商混淆配置 ***）
-keepattributes Signature
-ignorewarnings
-keep class javax.ws.rs.** { *; }
-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class org.json.** {*;}
-keep class com.ali.auth.** {*;}
-dontwarn com.ali.auth.**
-keep class com.taobao.securityjni.** {*;}
-keep class com.taobao.wireless.security.** {*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}
-keep interface mtopsdk.mtop.global.init.IMtopInitTask {*;}
-keep class * implements mtopsdk.mtop.global.init.IMtopInitTask {*;}
-keep class com.kepler.jd.**{ public <fields>; public <methods>; public *; }


-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-ignorewarnings

-keep class com.baidu.mobads.** { *; }
-keep class com.bun.miitmdid.core.** {*;}

-keep class com.style.widget.** {*;}
-keep class com.component.** {*;}

-keep class com.baidu.ad.magic.flute.** {*;}
-keep class com.baidu.mobstat.forbes.** {*;}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}


#FBReader native层调用java代码配置
-keep class org.geometerplus.zlibrary.core.library.ZLibrary {*;}
-keep class org.geometerplus.zlibrary.core.filesystem.ZLFile {*;}
-keep interface org.geometerplus.zlibrary.text.model.ZLTextModel {*;}
-keep class org.geometerplus.zlibrary.text.model.CachedCharStorageException {*;}
-keep class org.geometerplus.zlibrary.core.encodings.Encoding {*;}
-keep class org.geometerplus.zlibrary.core.encodings.EncodingConverter {*;}
-keep class org.geometerplus.zlibrary.core.encodings.JavaEncodingCollection {*;}
-keep class org.geometerplus.fbreader.formats.NativeFormatPlugin {*;}
-keep class org.geometerplus.fbreader.formats.PluginCollection {*;}
-keep class org.geometerplus.fbreader.Paths {*;}
-keep class org.geometerplus.fbreader.book.Book {*;}
-keep class org.geometerplus.fbreader.book.Tag {*;}
-keep class org.geometerplus.fbreader.bookmodel.NativeBookModel {*;}

#保持okhttp不被混淆
-dontwarn com.baidu.searchbox.novel.okhttp3.**
-keep class com.baidu.searchbox.novel.okhttp3.**{ *; }
-keep interface com.baidu.searchbox.novel.okhttp3.**{ *; }

#gson
-keepclassmembers,allowobfuscation class * {
 @com.baidu.searchbox.novel.gson.annotations.SerializedName <fields>;
}

# 单针对该类做keep处理
-keep public class com.baidu.android.common.util.Util { *; }

# 自定义控件
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class **.R$* {
 public static <fields>;
}

-keep class com.baidu.searchbox.reader.ReaderPluginApi{*;}
#UBC
-keep class com.baidu.ubc.* {*;}


-dontoptimize
-dontpreverify
-dontwarn com.duoyou.**
-keep class com.duoyou.**{*;}
-keep class **.R$* {
 *;
}
-keepclassmembers class **.R$* {
public static <fields>;
}
