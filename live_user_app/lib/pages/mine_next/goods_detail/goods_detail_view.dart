import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:flutter_html_all/flutter_html_all.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../services/user_info_ctl.dart';
import '../../../utils/toast_util.dart';
import '../../../widgets/cache_image.dart';
import 'goods_detail_logic.dart';

class GoodsDetailPage extends BaseStatefulWidget<GoodsDetailLogic> {
  const GoodsDetailPage({super.key});

  @override
  String? title() {
    return '商品详情';
  }

  @override
  Widget buildContent(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (controller.isShare!=null) {
          Get.back(result: 0);
        }else{
          Navigator.pop(context);
        }
      },
      child: _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    double width = MediaQuery.of(Get.context!).size.width;
    double height = MediaQuery.of(Get.context!).size.height;
    return Column(
      children: [
        Expanded(
          child: SingleChildScrollView(
            child: Column(
              children: [
                _buildSlider(),
                _buildInfo(),
                _buildDetail(),
                const SizedBox(height: 20.0)
              ],
            ),
          ),
        ),
        _buildBottom(),
      ],
    );
  }

  // 顶部轮播图
  Widget _buildSlider() {
    return Stack(
      children: [
        CarouselSlider(
            items: controller.goodsDetail.value.slider.map((item) {
              return CacheImage(
                item,
                width: double.infinity,
              );
            }).toList(),
            options: CarouselOptions(
                autoPlay: true,
                viewportFraction: 1,
                initialPage: 0,
                aspectRatio: 1.0,
                onPageChanged: controller.onSliderChanged)),
        Positioned(
          right: 8.0,
          bottom: 10.0,
          child: Container(
            height: 24.0,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            decoration: BoxDecoration(
                color: ColorUtil.black50,
                borderRadius: BorderRadius.circular(12)),
            child: Obx(()=> JhText.cText(
                  "${controller.currentIndex.value + 1} / ${controller.goodsDetail.value.slider.length}",
                  ColorUtil.white, 15.0),
            ),
          ),
        )
      ],
    );
  }

  Widget _buildInfo() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
      color: ColorUtil.white,
      child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
        Row(
          children: [
            JhText.cText("现价：￥${controller.goodsDetail.value.price}",
                ColorUtil.primaryColor, 18.0, fontWeight: FontWeight.bold),
            const SizedBox(width: 8.0),
            // 用横线横串数字
            Text(
              "原价：${controller.goodsDetail.value.costPrice}元",
              style: TextStyle(
                  color: ColorUtil.black54,
                  fontSize: 16.0,
                  decoration: TextDecoration.lineThrough,
                  decorationColor: ColorUtil.black54),
            ),
            const Spacer(),
            JhText.cText("销量：${controller.goodsDetail.value.sales}",
                ColorUtil.black54, 16.0)
          ],
        ),
        const SizedBox(height: 8.0),
        JhText.cText(controller.goodsDetail.value.title,
            ColorUtil.black, 18.0, fontWeight: FontWeight.bold),
      ]),
    );
  }

  Widget _buildDetail() {
    return Html(
      data: controller.goodsDetail.value.detail,
      style: {
        "table": Style(
          backgroundColor: const Color.fromARGB(0x50, 0xee, 0xee, 0xee),
        ),
        "th": Style(
          padding: HtmlPaddings.all(6),
          backgroundColor: ColorUtil.grey,
        ),
        "td": Style(
          padding: HtmlPaddings.all(6),
          border: const Border(bottom: BorderSide(color: ColorUtil.grey)),
        ),
        'h5': Style(maxLines: 2, textOverflow: TextOverflow.ellipsis),
        'flutter': Style(
          display: Display.block,
          fontSize: FontSize(5, Unit.em),
        ),
        ".second-table": Style(
          backgroundColor: ColorUtil.transparent,
        ),
        ".second-table tr td:first-child": Style(
          fontWeight: FontWeight.bold,
          textAlign: TextAlign.end,
        ),
      },
      extensions: [
        TagExtension(
            tagsToExtend: {"img"},
            builder: (context) {
              return CacheImage(
                context.attributes['src']??'',
              );
            }),
        TagWrapExtension(
            tagsToWrap: {"table"},
            builder: (child) {
              return SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: child,
              );
            }),
        TagExtension(
          tagsToExtend: {"tex"},
          builder: (context) => Math.tex(
            context.innerHtml,
            mathStyle: MathStyle.display,
            textStyle: context.styledElement?.style.generateTextStyle(),
            onErrorFallback: (FlutterMathException e) {
              return JhText.cText(e.message, ColorUtil.black, 14.0);
            },
          ),
        ),
        TagExtension.inline(
          tagsToExtend: {"bird"},
          child: const TextSpan(text: "🐦"),
        ),
        TagExtension(
          tagsToExtend: {"flutter"},
          builder: (context) => CssBoxWidget(
            style: context.styledElement!.style,
            child: FlutterLogo(
              style: context.attributes['horizontal'] != null
                  ? FlutterLogoStyle.horizontal
                  : FlutterLogoStyle.markOnly,
              textColor: context.styledElement!.style.color!,
              size: context.styledElement!.style.fontSize!.value,
            ),
          ),
        ),
        const MathHtmlExtension(),
        const AudioHtmlExtension(),
        const VideoHtmlExtension(),
        const IframeHtmlExtension(),
        const TableHtmlExtension(),
        const SvgHtmlExtension(),
      ],
    );
    // return CachedNetworkImage(
    //   imageUrl: controller.goodsDetail.value.detail,
    //   width: double.infinity,
    //   fit: BoxFit.cover,
    // );
  }

  Widget _buildBottom() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 10.0),
      color: ColorUtil.white,
      child: Row(
        children: [
          InkWell(
            onTap: () {
              Get.toNamed(AppPages.contactPage);
            },
            child: Column(
              children: [
                Icon(Icons.chat, color: ColorUtil.black54, size: 20.0),
                SizedBox(width: 4.0),
                JhText.cText("客服",
                    ColorUtil.black54, 14.0)
              ],
            ),
          ),
          SizedBox(width: 20.w,),
          Clickable(
            child: Column(
              children: [
                Icon(Icons.share, color: ColorUtil.black54, size: 20.0),
                JhText.cText('分享', ColorUtil.black54, 14),
              ],
            ),
            onTap: () {
              if (UserInfoCtl.to.isLogin) {
                if(kIsWeb){
                  ToastUtil.showToast("请在app中分享");
                }else{
                  controller.getUserPoster();
                }
              } else {
                ToastUtil.showToast("请先登录");
              }
            },
          ),
          const Spacer(),
          InkWell(
            onTap: () {
              Map<String,dynamic> params = {};
              if(controller.type==1){
                ///商品详情
                params = {
                  'id':controller.goodsDetail.value.id,
                  'price':controller.goodsDetail.value.price,
                  'liveId':null,
                  'cover':controller.goodsDetail.value.hostImage,
                  'title':controller.goodsDetail.value.title,
                };

               }else if(controller.type==0){
                params = {
                  'id':controller.liveGoods.id,
                  'price':controller.liveGoods.price,
                  'liveId':controller.liveGoods.liveId,
                  'cover':controller.liveGoods.cover,
                  'title':controller.liveGoods.title,
                };
              }
              Get.toNamed(AppPages.createOrderPage, arguments:params);
              // navigator?.push(FadeRoute(
              //     effect: 'bottom',
              //     child: PopupOrder(
              //       goodsId: controller.goodsDetail.value.id,
              //
              //     )
              // ));
            },
            child: Container(
              height: 50.0,
              alignment: Alignment.center,
              margin: EdgeInsets.only(bottom: MediaQuery.of(Get.context!).padding.bottom),
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              decoration: BoxDecoration(
                color: ColorUtil.primaryColor,
                borderRadius: BorderRadius.circular(8.0),
              ),
              child: JhText.cText("立即购买",
                  ColorUtil.white, 18.0, fontWeight: FontWeight.w500),
            ),
          ),
        ],
      ),
    );
  }
}
