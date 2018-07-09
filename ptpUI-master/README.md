# ptpUI
## 页面一
- 活动：MainActicity
- 布局：activity——main.xml
- 控件id：
  - `toolbar`：左边是个滑动菜单（菜单内部UI还没写）
  - `vitamio_videoView`：用Vitamio库获取视频流
  - `btn_take_photo`：拍照
  - `settings`：输入视频流地址
  - `photo`：查看相册

## 页面二
- 活动：AlbumActivity
- 布局：activity_album.xml, layout_photos.xml
- 控件id：
  - `photo_image`：照片
  - `photo_name`：暂时只放照片名，后续可改为照片信息，或点击照片查看详细信息
  
## TODO
1. 拍照进度条、成功提示
2. 相册界面标题栏
3. 地址输入错误提示
4. 标题栏Info菜单，点击查看当前视频流信息
  
