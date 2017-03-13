中欧ERP 效果图展示：
====
说明：1.帐号密码为测试版，所有功能都由效果图概括了，看源码即可，若看readme.m不方便，可以下载项目根目录的readmepic图片文件夹浏览
2.所有关于listView的都可以上拉下拉刷新
3.客官嫌弃图片大的晃瞎眼，ctrl+滑轮<^+^>

一登陆界面：
----
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E7%99%BB%E5%BD%9501.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E7%99%BB%E5%BD%9502.jpg)

二主界面：
----
#1.个人设置界面：
navaigation侧滑显示：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%B8%AA%E4%BA%BA%E8%AE%BE%E7%BD%AEnavigation%E7%95%8C%E9%9D%A201.jpg)
个人信息：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%B8%AA%E4%BA%BA%E8%AF%A6%E7%BB%86%E4%BF%A1%E6%81%AF.jpg)
修改密码：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BF%AE%E6%94%B9%E5%AF%86%E7%A0%81.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BF%AE%E6%94%B9%E5%AF%86%E7%A0%8102.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BF%AE%E6%94%B9%E5%AF%86%E7%A0%8103.jpg)

#2.消息主界面：
点击公告/通知，跳转到详情界面，效果同 应用主界面进入详情界面
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%B6%88%E6%81%AF%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%B6%88%E6%81%AF%E4%B8%BB%E7%95%8C%E9%9D%A202.jpg)
#3通讯录主界面:
逻辑：主界面显示的是子公司list+公司所有的联系人，子公司list是以headview方式添加到listView中的
子公司-->部门-->部门通讯录——>个人详情
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%80%9A%E8%AE%AF%E5%BD%95%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
子公司下的部门：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%AD%90%E5%85%AC%E5%8F%B8%E4%B8%8B%E7%9A%84%E9%83%A8%E9%97%A8%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%AD%90%E5%85%AC%E5%8F%B8%E4%B8%8B%E7%9A%84%E9%83%A8%E9%97%A8%E7%95%8C%E9%9D%A202.jpg)
部门下的通讯录：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%83%A8%E9%97%A8%E4%B8%8B%E7%9A%84%E9%80%9A%E8%AE%AF%E5%BD%95%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%83%A8%E9%97%A8%E4%B8%8B%E7%9A%84%E9%80%9A%E8%AE%AF%E5%BD%95%E7%95%8C%E9%9D%A202.jpg)
通讯录详情，可以打电话，发短信：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%80%9A%E8%AE%AF%E5%BD%95%E8%AF%A6%E7%BB%86%E7%95%8C%E9%9D%A201.jpg)
#4应用主界面：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
##1.审批（项目重点）
包括 我的申请 我的审批 抄送给我 和13个移动端申请功能；申请功能模式一样，界面参数大同小异；
我的审批 可以处理低级别员工发过来的申请，包括 同意 驳回 转交和抄送，每一条记录都可以查看详情

###审批主界面：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%AE%A1%E6%89%B9%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
###13个移动端申请：
申请界面模式一样，时间选择用时间轴dialog，选择类型用spinner/dialog,图片调用系统相机和相册，这里只显示一个申请就明白：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%8B%9B%E8%81%98%E7%94%B3%E8%AF%B701.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1%E7%94%B3%E8%AF%B7%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
###我的申请：
用于查看自己写的申请记录，包括13个移动端和9个web端的22个申请显示（我的审批、抄送给我功能也各22个界面），这里只选择一个展示就明白：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E7%94%B3%E8%AF%B7%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E7%94%B3%E8%AF%B7_%E8%AF%B7%E5%81%87%E8%AF%A6%E6%83%85.jpg)
###我的审批:
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
未审批：驳回 同意 转交
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E8%AF%B7%E5%81%8701.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E8%AF%B7%E5%81%87_%E5%90%8C%E6%84%8F01.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E8%AF%B7%E5%81%87_%E8%BD%AC%E4%BA%A4%E9%80%9A%E8%AE%AF%E5%BD%9501.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E8%AF%B7%E5%81%87_%E9%A9%B3%E5%9B%9E01.jpg)
已审批：抄送
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E9%80%9A%E7%9F%A5%E5%85%AC%E5%91%8A02.jpg)
调用通讯录
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%88%91%E7%9A%84%E5%AE%A1%E6%89%B9_%E9%80%9A%E7%9F%A5%E5%85%AC%E5%91%8A_%E6%8A%84%E9%80%8101.jpg)
###我的抄送：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E6%8A%84%E9%80%81%E7%BB%99%E6%88%91%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)

##2.采购领用主界面：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E9%87%87%E8%B4%AD%E9%A2%86%E7%94%A8%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E9%87%87%E8%B4%AD%E9%A2%86%E7%94%A8%E4%B8%BB%E7%95%8C%E9%9D%A202.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E9%87%87%E7%94%A8%E9%A2%86%E7%94%A8%E4%B8%BB%E7%95%8C%E9%9D%A203.jpg)
详情：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%87%87%E8%B4%AD%E8%AF%A6%E6%83%8502.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%A2%86%E7%94%A8%E8%AF%A6%E6%83%8501.jpg)
##3.日程主界面：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%97%A5%E7%A8%8B%E4%B8%BB%E7%95%8C%E9%9D%A201.png)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%B7%BB%E5%8A%A0%E6%97%A5%E7%A8%8B01.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%B7%BB%E5%8A%A0%E6%97%A5%E7%A8%8B_%E9%80%89%E6%8B%A9%E7%B1%BB%E5%9E%8B01.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%B7%BB%E5%8A%A0%E6%97%A5%E7%A8%8B_%E6%8F%90%E9%86%92%E6%AC%A1%E6%95%B001.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%B7%BB%E5%8A%A0%E6%97%A5%E7%A8%8B_%E6%8F%90%E9%86%92%E6%97%B6%E9%97%B4.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%97%A5%E7%A8%8B%E6%B7%BB%E5%8A%A0%E5%90%8E%E8%AF%A6%E6%83%8501.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%BA%94%E7%94%A8_%E6%97%A5%E7%A8%8B%E8%A1%A801.jpg)
##4.财务主界面：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1%E4%B8%BB%E7%95%8C%E9%9D%A202.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1_%E8%AF%A6%E6%83%8501.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1_%E8%AF%A6%E6%83%8502.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E8%B4%A2%E5%8A%A1_%E8%AF%A6%E6%83%8503.jpg)
##5.通知/公告：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%85%AC%E5%91%8A%E4%B8%BB%E7%95%8C%E9%9D%A2.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%85%AC%E5%91%8A%E8%AF%A6%E6%83%85.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%80%9A%E7%9F%A5%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E9%80%9A%E7%9F%A5%E8%AF%A6%E6%83%85.jpg)
##6.外出考勤：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4%E4%B8%BB%E7%95%8C%E9%9D%A2.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4_%E5%9C%B0%E5%9B%BE%E7%AD%BE%E5%88%B002.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4_%E5%9C%B0%E5%9B%BE%E7%AD%BE%E5%88%B003.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4_%E5%9C%B0%E5%9B%BE%E7%AD%BE%E5%88%B001.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4_%E6%89%80%E6%9C%89%E8%AE%B0%E5%BD%9501.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E5%A4%96%E5%87%BA%E8%80%83%E5%8B%A4_%E6%89%80%E6%9C%89%E8%AE%B0%E5%BD%9502.jpg)
##7.交车，包括用车申请和车辆维保申请，点击进入指定详情,未交车状态下，进入交车界面，提交数据，已交车状态下，进入浏览界面查看
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BA%A4%E8%BD%A6%E4%B8%BB%E7%95%8C%E9%9D%A201.jpg)
已交车详情：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BA%A4%E8%BD%A6_%E5%B7%B2%E4%BA%A4%E8%BD%A6%E8%AF%A6%E6%83%8501.jpg)
未交车_交车操作：
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BA%A4%E8%BD%A6_%E6%8F%90%E4%BA%A4%E7%95%8C%E9%9D%A2.jpg)
![image](https://github.com/66668/ZOERP/blob/master/readmepic/%E4%BA%A4%E8%BD%A6_%E6%8F%90%E4%BA%A4%E8%AF%A6%E7%BB%86%E7%95%8C%E9%9D%A201.jpg)
#5推送：
使用激光推送，具体就不展示了，注意menifest，gradle,libs绑定，myApplication初始化和设置别名即可用

