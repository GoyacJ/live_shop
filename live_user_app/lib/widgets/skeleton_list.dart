import 'package:flutter/material.dart';
import 'skeleton_container.dart';

class SkeletonList extends StatelessWidget {
  final int itemCount;
  final EdgeInsetsGeometry padding;
  final double itemSpacing;

  const SkeletonList({
    super.key,
    this.itemCount = 5,
    this.padding = const EdgeInsets.all(16.0),
    this.itemSpacing = 16.0,
  });

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      padding: padding,
      itemCount: itemCount,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemBuilder: (context, index) {
        return Padding(
          padding: EdgeInsets.only(bottom: itemSpacing),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // 左侧方形
              const SkeletonContainer(
                width: 80,
                height: 80,
                borderRadius: 8,
              ),
              const SizedBox(width: 12),
              // 右侧内容
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const SkeletonContainer(
                      width: double.infinity,
                      height: 24,
                      borderRadius: 4,
                    ),
                    const SizedBox(height: 8),
                    SkeletonContainer(
                      width: MediaQuery.of(context).size.width * 0.6,
                      height: 16,
                      borderRadius: 4,
                    ),
                    const SizedBox(height: 8),
                    SkeletonContainer(
                      width: MediaQuery.of(context).size.width * 0.4,
                      height: 16,
                      borderRadius: 4,
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}

// 使用示例
class SkeletonListExample extends StatelessWidget {
  const SkeletonListExample({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: SkeletonList(
        itemCount: 3,
        padding: EdgeInsets.all(16),
        itemSpacing: 16,
      ),
    );
  }
}
