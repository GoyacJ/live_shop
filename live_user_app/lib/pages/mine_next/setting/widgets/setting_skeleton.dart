import 'package:flutter/material.dart';
import '../../../../utils/color_util.dart';
import '../../../../widgets/skeleton_container.dart';

class SettingSkeleton extends StatelessWidget {
  const SettingSkeleton({super.key});

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: [
        // 用户信息卡片骨架
        _buildUserCardSkeleton(),
        const SizedBox(height: 16),
        // 设置列表骨架
        _buildSettingsListSkeleton(),
      ],
    );
  }

  // 构建用户信息卡片骨架
  Widget _buildUserCardSkeleton() {
    return Container(
      padding: const EdgeInsets.all(16),
      color: ColorUtil.white,
      child: const Row(
        children: [
          // 头像骨架
          SkeletonContainer(
            width: 60,
            height: 60,
            borderRadius: 30,
          ),
          SizedBox(width: 16),
          // 用户信息骨架
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SkeletonContainer(
                  width: 100,
                  height: 20,
                ),
                SizedBox(height: 8),
                SkeletonContainer(
                  width: 150,
                  height: 16,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // 构建设置列表骨架
  Widget _buildSettingsListSkeleton() {
    return Container(
      color: ColorUtil.white,
      child: Column(
        children: List.generate(
          5,
          (index) => Column(
            children: [
              const ListTile(
                leading: SkeletonContainer(
                  width: 24,
                  height: 24,
                  borderRadius: 12,
                ),
                title: SkeletonContainer(
                  width: 120,
                  height: 16,
                ),
                trailing: SkeletonContainer(
                  width: 80,
                  height: 16,
                ),
              ),
              if (index < 4) const Divider(height: 1),
            ],
          ),
        ),
      ),
    );
  }
}
