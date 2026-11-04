import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:timelines_plus/timelines_plus.dart';
import '../../../utils/color_util.dart';
import 'logistics_info_logic.dart';

class LogisticsInfoPage extends BaseStatefulWidget<LogisticsInfoLogic> {
  const LogisticsInfoPage({super.key});

  @override
  String? title() {
    return '物流信息';
  }

  @override
  Widget buildContent(BuildContext context) {
    return controller.model!=null?CustomScrollView(
      slivers: [
        SliverToBoxAdapter(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              itemView(
                  'assets/images/ic_courier.png',
                  controller.model?.deliveryMan ?? '快递员',
                  controller.model?.deliveryManTel ?? ''),
              itemView(
                  'assets/images/ic_courier_truck.png',
                  controller.model?.shipperCode ?? '',
                  controller.model?.logisticCode ?? ''),
              Timeline.tileBuilder(
                physics: NeverScrollableScrollPhysics(),
                shrinkWrap: true,
                theme: TimelineThemeData(
                  nodePosition: 0,
                  connectorTheme: const ConnectorThemeData(
                    thickness: 3.0,
                    color: Color(0xffd3d3d3),
                  ),
                  indicatorTheme: const IndicatorThemeData(
                    size: 15.0,
                  ),
                ),
                padding: EdgeInsets.symmetric(vertical: 0.0, horizontal: 30.w),
                builder: TimelineTileBuilder.connected(
                  contentsBuilder: (_, index) => Container(
                      margin: const EdgeInsets.only(left: 10.0,bottom: 20,top: 10),

                      // decoration: BoxDecoration(
                      //   borderRadius: BorderRadius.circular(2.0),
                      //   color: const Color(0xffe6e7e9),
                      // ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          JhText.cText(controller.model!.traces![index].acceptTime,
                              ColorUtil.black, 13),
                          JhText.cText(
                              controller.model!.traces![index].acceptStation,
                              ColorUtil.black,
                              13),
                          JhText.cText(controller.model!.traces![index].location,
                              ColorUtil.black, 13),
                        ],
                      )),
                  connectorBuilder: (_, index, __) {
                    return SolidLineConnector(
                      thickness: 1.w,
                      color: ColorUtil.grey.shade300,
                    );
                    // if (index == 0) {
                    //   return SolidLineConnector(
                    //       thickness: 2.w, color: ColorUtil.primaryColor);
                    // } else {
                    //   return SolidLineConnector(
                    //     thickness: 2.w,
                    //     color: ColorUtil.grey.shade300,
                    //   );
                    // }
                  },
                  indicatorBuilder: (_, index) {
                    return index == 0
                        ? const DotIndicator(
                      size: 15,
                      color: Color(0xff6ad192),
                      child: Icon(
                        Icons.check,
                        color: ColorUtil.white,
                        size: 10.0,
                      ),
                    )
                        :  OutlinedDotIndicator(
                      size: 15,
                      color: ColorUtil.grey.shade300,
                      backgroundColor: ColorUtil.grey.shade300,
                    );
                  },
                  itemCount: controller.model?.traces?.length ?? 0,
                ),
              )
            ],
          ),
        )
      ],
    ):_buildNull();
  }

  itemView(icon, title, content) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 30.w),
      margin: EdgeInsets.only(bottom: 20.w),
      child: Row(
        children: [
          Image.asset(
            icon,
            width: 20.w,
            height: 20.w,
          ),
          SizedBox(
            width: 5.w,
          ),
          JhText.cText(title, ColorUtil.black, 14),
          const Spacer(),
          JhText.cText(content, ColorUtil.black, 14),
        ],
      ),
    );
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 300.w,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Image.asset("assets/images/empty.png",
              height: 100.0, fit: BoxFit.contain),
          const Text("暂无记录",
              style: TextStyle(fontSize: 16.0, color: ColorUtil.black38))
        ],
      ),
    );
  }}
