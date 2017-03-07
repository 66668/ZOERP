package com.zhongou.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.zhongou.application.MyApplication;
import com.zhongou.dialog.ChooseItemDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 图片处理
 * 
 * @author JackSong
 * 
 */
public class CameraGalleryUtils {
	public static final int REQUEST_CODE_PICK_PHOTO_FROM_MYCAMERA = 69;// 自定义相机调用的常量
	public static final int REQUEST_CODE_PICK_PHOTO_FROM_CAMERA = 70;// 系统相机调用的常量
	public static final int REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS = 71;// 相册常量
	public static final int REQUEST_CODE_PICK_PHOTO_FINISH_HANDLED = 72;// 已处理图片显示常量
	public static final int REQUEST_CODE_PICK_PHOTO_FINISH_UNHANDLED = 73;// 已处理图片显示常量

	public static final int IMG_TYPE_CAMERA = -99;// 拍照
	public static final int IMG_TYPE_GALLERY = -98;// 相册
	public static final int BACKBEFORE = -97;
	private Activity context;
	private int updateType;
	private ChoosePicCallBack callBack;

	// 构造赋值RegisterActivity.oncreate方法调用
	public CameraGalleryUtils(Activity context, ChoosePicCallBack callBack) {
		this.context = context;
		this.callBack = callBack;

	}

	// 接口方法
	public interface ChoosePicCallBack {
		void updateAvatarSuccess(int updateType, String avatar, String avatarBase64);

		void updateAvatarFailed(int updateType);

		void cancel();
	}
	/**
	 * 选取拍照对话框
	 * @param updateType
	 */
	public void showChoosePhotoDialog(int updateType) {

		this.updateType = updateType;// -99
		// 实例化ChooseItemDialog
		ChooseItemDialog dialog = new ChooseItemDialog(context, "拍一张", "选一张", 0, false, new ChooseItemDialog.ChooseItemDialogCallBack() {// 自定义接口
			// 实现父类ChooseItemDialog.ChooseItemDialogCallBack的两个接口方法（1）
			@Override
			public void confirm(int whichItem) {
				if (whichItem == 0) {
					// 自定义相机 69
//					Intent MyCameraIntent = new Intent(context, MyCameraActivity.class);//MyCameraActivity
//					context.startActivityForResult(MyCameraIntent, REQUEST_CODE_PICK_PHOTO_FROM_MYCAMERA);// 69
				} else if (whichItem == 1) {// 相册
					Intent galleryIntent = new Intent(Intent.ACTION_PICK, null);// 相册
					// 设置可以浏览的图片类型：.jpeg/.jpg
					galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					context.startActivityForResult(galleryIntent, REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS);// 71
				}
			}

			// 实现父类ChooseItemDialog.ChooseItemDialogCallBack的两个接口方法（2）
			@Override
			public void cancel() {
				if (callBack != null) {
					callBack.cancel();
				}
			}
		});
		dialog.show();
	}
	/**
	 * 返回数据的处理
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 */
	public void onActivityResultAction(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_MYCAMERA) {// 69自定义相机
			if (resultCode == Activity.RESULT_OK) {//resultCode == Activity.RESULT_OK换成自定义
//				Intent i = new Intent();
//				i.setClass(context, PhotoClipPictureActivity.class);// 跳转图片剪裁
//				context.startActivityForResult(i, REQUEST_CODE_PICK_PHOTO_FINISH_HANDLED);// 72
			}
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_CAMERA) {// 70系统相机
			// 跳转图片剪裁
			if (resultCode == Activity.RESULT_OK) {
//				Intent i = new Intent();
//				i.setClass(context, PhotoClipPictureActivity.class);// 跳转图片剪裁
//				context.startActivityForResult(i, REQUEST_CODE_PICK_PHOTO_FINISH_HANDLED);// 72
			}
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FROM_ALBUMS) {// 71相册
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = intent.getData();

				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					try {
						// 获得用户选择的图片的索引值
						int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						// 将光标移至开头 ，这个很重要，不小心很容易引起越界s
						cursor.moveToFirst();
						// 最后根据索引值获取图片路径
						final String img_path = cursor.getString(actual_image_column_index);// 相册图片路径

//						Intent i = new Intent();
//						i.putExtra("img_path", img_path);// 图片路径
//						i.setClass(context, PhotoClipPictureActivity.class);// 跳转图片剪裁
//						context.startActivityForResult(i, REQUEST_CODE_PICK_PHOTO_FINISH_HANDLED);// 72

					} catch (Exception e) {
						Toast.makeText(context, "请选择相册图片", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "请选择相册图片", Toast.LENGTH_SHORT).show();
				}

			}
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FINISH_HANDLED) {// 72，图片处理后显示
			if (resultCode == Activity.RESULT_OK) {
				// 已处理图像路径：
				String picPath = MyApplication.getHandledUserPhotoPath(context);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				File picFile = new File(picPath);
				String avatarBase64 = "";
				if (picFile.exists()) {
					try {
						FileInputStream fis = new FileInputStream(picFile);
						byte[] buffer = new byte[128];
						int iLength = 0;
						while ((iLength = fis.read(buffer)) != -1) {
							baos.write(buffer, 0, iLength);
						}
						avatarBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
						fis.close();
						baos.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				// 显示图片(子类实现)
				callBack.updateAvatarSuccess(updateType, picPath, avatarBase64);
			}
		} else if (requestCode == REQUEST_CODE_PICK_PHOTO_FINISH_UNHANDLED) {// 73/ 未处理图片显示
			if (resultCode == Activity.RESULT_OK) {
				// 未处理图像路径：
				String picPath = MyApplication.getUnhandledUserPhotoPath(context);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				File picFile = new File(picPath);
				String avatarBase64 = "";
				if (picFile.exists()) {
					try {
						FileInputStream fis = new FileInputStream(picFile);
						byte[] buffer = new byte[128];
						int iLength = 0;
						while ((iLength = fis.read(buffer)) != -1) {
							baos.write(buffer, 0, iLength);
						}
						avatarBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
						fis.close();
						baos.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				// 显示图片
				callBack.updateAvatarSuccess(updateType, picPath, avatarBase64);
			}
		}
	}
}
