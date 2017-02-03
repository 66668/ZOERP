package com.zhongou.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongou.R;
import com.zhongou.application.MyApplication;
import com.zhongou.common.HttpParameter;
import com.zhongou.common.HttpResult;
import com.zhongou.common.MyException;
import com.zhongou.common.NetworkManager;
import com.zhongou.db.entity.UserEntity;
import com.zhongou.model.ContactsDeptModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;
import com.zhongou.model.MapAttendModel;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.applicationdetailmodel.BorrowModel;
import com.zhongou.model.applicationdetailmodel.ConferenceModel;
import com.zhongou.model.applicationdetailmodel.ContractFileModel;
import com.zhongou.model.applicationdetailmodel.DismissionModel;
import com.zhongou.model.applicationdetailmodel.LeaveModel;
import com.zhongou.model.applicationdetailmodel.LoanReimbursementModel;
import com.zhongou.model.applicationdetailmodel.NotificationAndNoticeModel;
import com.zhongou.model.applicationdetailmodel.OfficeModel;
import com.zhongou.model.applicationdetailmodel.OutGoingModel;
import com.zhongou.model.applicationdetailmodel.PositionReplaceModel;
import com.zhongou.model.applicationdetailmodel.ProcurementModel;
import com.zhongou.model.applicationdetailmodel.ReceiveModel;
import com.zhongou.model.applicationdetailmodel.RecruitmentModel;
import com.zhongou.model.applicationdetailmodel.RetestModel;
import com.zhongou.model.applicationdetailmodel.SalaryAjustModel;
import com.zhongou.model.applicationdetailmodel.TakeDaysOffModel;
import com.zhongou.model.applicationdetailmodel.VehicleMaintainModel;
import com.zhongou.model.applicationdetailmodel.VehicleModel;
import com.zhongou.model.applicationdetailmodel.WorkOverTimeModel;
import com.zhongou.model.approvaldetailmodel.BorrowApvlModel;
import com.zhongou.model.approvaldetailmodel.ConferenceApvlModel;
import com.zhongou.model.approvaldetailmodel.ContractFileApvlModel;
import com.zhongou.model.approvaldetailmodel.DismissionApvlModel;
import com.zhongou.model.approvaldetailmodel.LeaveApvlModel;
import com.zhongou.model.approvaldetailmodel.NotificationAndNoticeApvlModel;
import com.zhongou.model.approvaldetailmodel.OUtGoingApvlModel;
import com.zhongou.model.approvaldetailmodel.OfficeApvlModel;
import com.zhongou.model.approvaldetailmodel.ProcurementApvlModel;
import com.zhongou.model.approvaldetailmodel.ReceiveApvlModel;
import com.zhongou.model.approvaldetailmodel.RecruitmentApvlModel;
import com.zhongou.model.approvaldetailmodel.RetestApvlModel;
import com.zhongou.model.approvaldetailmodel.SalaryAjustApvlModel;
import com.zhongou.model.approvaldetailmodel.TakeDaysOffApvlModel;
import com.zhongou.model.approvaldetailmodel.VehicleApvlModel;
import com.zhongou.model.approvaldetailmodel.VehicleMaintainApvlModel;
import com.zhongou.model.approvaldetailmodel.WorkOverTimeApvlModel;
import com.zhongou.utils.APIUtils;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.JSONUtils;
import com.zhongou.utils.Utils;
import com.zhongou.utils.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * 用户管理者帮助类
 * <p/>
 * 处理访问服务端信息类 解析js对象，调用Gson外部包：gson-2.2.2.jar
 *
 * @author JackSong
 */
public class UserHelper<T> {
    static UserEntity mCurrentUser = null;
    static String configUserManager = null;//

    /**
     * (2)获取用户账号
     *
     * @return
     */
    public static UserEntity getCurrentUser() {
        // 调用下边的方法
        return getCurrentUser(true);
    }

    public static UserEntity getCurrentUser(boolean isAutoLoad) {

        if (mCurrentUser == null && isAutoLoad) {// 判断MemberModel类是否为空
            // 中断保存
            ConfigUtil config = new ConfigUtil(MyApplication.getInstance());// 中断保存获取信息
            String workId = config.getWorkId();
            if (!"".equals(workId)) {
                // 获取所有当前用户信息，保存到mCurrentUser对象中
                mCurrentUser = config.getUserEntity();
                Log.d("SJY", "获取当前存储getCurrentUser");
            }
        }
        return mCurrentUser;
    }

    public static void setmCurrentUser(UserEntity u) {//退出登录调用
        mCurrentUser = u;
    }

    /**
     * 01 密码登录
     *
     * @param context
     * @param storeId
     * @param workId
     * @param password
     * @throws MyException
     */
    public static void loginByPs(Context context, String storeId, String workId, String password) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        HttpResult hr = APIUtils.postForObject(WebUrl.UserManager.LOGIN_POST,
                HttpParameter.create().
                        add("storeId", storeId).
                        add("workId", workId).
                        add("password", password));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //返回个人信息保存
        UserEntity uEntity = new UserEntity();

        uEntity.setEmployeeID(JSONUtils.getString(hr.jsonObject, "EmployeeID"));
        uEntity.setStoreID(JSONUtils.getString(hr.jsonObject, "StoreID"));
        uEntity.setStoreUserId(JSONUtils.getString(hr.jsonObject, "StoreUserId"));

        uEntity.setName(JSONUtils.getString(hr.jsonObject, "Name"));
        uEntity.setTelephone(JSONUtils.getString(hr.jsonObject, "Telephone"));
        uEntity.setEmail(JSONUtils.getString(hr.jsonObject, "Email"));
        uEntity.setStoreName(JSONUtils.getString(hr.jsonObject, "StoreName"));
        uEntity.setJobNumber(JSONUtils.getString(hr.jsonObject, "JobNumber"));
        uEntity.setDepartmentName(JSONUtils.getString(hr.jsonObject, "DepartmentName"));
        uEntity.setPostName(JSONUtils.getString(hr.jsonObject, "PostName"));
        uEntity.setEntryDate(JSONUtils.getString(hr.jsonObject, "EntryDate"));

        //登录信息保存
        uEntity.setstoreId(storeId);
        uEntity.setWorkId(workId);
        uEntity.setPassword(password);
        Log.d("SJY", "UserEntity--StoreID=" + uEntity.getStoreID());

        // ConfigUtil中断保存，在退出后重新登录用getAccount()调用
        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        config.setStoreId(storeId);// 保存公司编号
        config.setWorkId(workId);// 保存工号
        config.setPassword(password);
        config.setAutoLogin(true);
        config.setUserEntity(uEntity);// 保存已经登录成功的对象信息
        mCurrentUser = uEntity;// 将登陆成功的对象信息，赋值给全局变量
    }

    /**
     * 02添加地图考勤,（obj形式上传)
     *
     * @param context
     * @param attendCapTime
     * @param address
     * @throws MyException
     */
    public static void forAttend(Context context, String attendCapTime, String address) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            /**
             * 参数保存成json
             */
            JSONObject js = new JSONObject();
            js.put("EmployeeID", mCurrentUser.getEmployeeID());
            js.put("attendCapTime", attendCapTime);
            js.put("address", address);

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.ATTENDRECORD,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 03 获取地图考勤记录
     *
     * @param context
     * @param iMaxTime
     * @param iMinTime
     * @param pageSize
     * @return
     * @throws MyException
     */
    public static List<MapAttendModel> getAttendRecord(Context context, String iMaxTime, String iMinTime, String pageSize) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETATTENDRECORD,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", mCurrentUser.getStoreID())
                        .add("employeeId", mCurrentUser.getEmployeeID())
                        .add("pageSize", pageSize));

        if (hr.hasError()) {
            throw hr.getError();
        }
        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MapAttendModel>>() {
        }.getType());
    }

    /**
     * 03 获取 我的申请
     * <p>
     *
     * @param context
     * @param iMaxTime
     * @param iMinTime
     * @return
     * @throws MyException
     */
    public static List<MyApplicationModel> GetMyApplicationSearchResults(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPLICATIONRECORD,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", mCurrentUser.getStoreID())
                        .add("employeeId", mCurrentUser.getEmployeeID())
                        .add("pageSize", 20));

        if (hr.hasError()) {
            throw hr.getError();
        }
        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApplicationModel>>() {
        }.getType());
    }

    /**
     * 04 获取 我的审批
     * <p></p>
     *
     * @param context
     * @param iMaxTime
     * @param iMinTime
     * @return
     * @throws MyException
     */
    public static List<MyApprovalModel> getApprovalSearchResults(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        try {
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALRECORD,
                    HttpParameter.create()
                            .add("iMaxTime", iMaxTime)
                            .add("iMinTime", iMinTime)
                            .add("storeID", mCurrentUser.getStoreID())
                            .add("employeeId", mCurrentUser.getEmployeeID())
                            .add("pageSize", 20));

            if (hr.hasError()) {
                throw hr.getError();
            }
            return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApprovalModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    //    public static Object ApplicationDetailPost(Context context,Object obj, String ApplicationID, String ApplicationType, String StoreID, String EmployeeID) throws MyException {
    //        if (!NetworkManager.isNetworkAvailable(context)) {
    //            throw new MyException(R.string.network_invalid);
    //        }
    //        try {
    //            Object obj2 =obj;
    //            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
    //                    HttpParameter.create().
    //                            add("ApplicationID", ApplicationID).
    //                            add("ApplicationType",ApplicationType).
    //                            add("StoreID",StoreID).
    //                            add("EmployeeID",EmployeeID));
    //
    //
    //            if (httpResult.hasError()) {
    //                throw httpResult.getError();
    //            }
    //            Log.d("HTTP", httpResult.jsonObject.toString());
    //
    //            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<T>() {}.getType());
    //        } catch (MyException e) {
    //            throw new MyException(e.getMessage());
    //        }
    //    }


    /**
     * 05-01 招聘申请 （obj形式上传）
     * <P></>
     *
     * @param context
     * @param jsonObject
     * @throws MyException
     */
    public static void recruitmentPost(Context context, JSONObject jsonObject) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--招聘--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json
             */
            jsonObject.put("CreateTime", Utils.getCurrentTime());//申请时间
            jsonObject.put("AppEmployeeID", mCurrentUser.getEmployeeID());
            jsonObject.put("StoreID", mCurrentUser.getStoreID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.RECRUITMENTPOST,
                    HttpParameter.create().add("obj", jsonObject.toString())
            );

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-02 离职申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param jsonObject
     * @throws MyException
     */
    public static void dimissionPost(Context context, JSONObject jsonObject) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--离职申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json
             */
            jsonObject.put("CreateTime", Utils.getCurrentTime());
            jsonObject.put("StoreID", mCurrentUser.getStoreID());
            jsonObject.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.DIMISSIONPOST,
                    HttpParameter.create().add("obj", jsonObject.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-03 请假申请 （obj形式上传）
     * <p></p>
     *
     * @param context
     * @param jsonObject
     * @throws MyException
     */
    public static void leavePost(Context context, JSONObject jsonObject) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--请假申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json
             */


            jsonObject.put("CreateTime", Utils.getCurrentTime());
            jsonObject.put("StoreID", mCurrentUser.getStoreID());
            jsonObject.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.LEAVEPOST,
                    HttpParameter.create().add("obj", jsonObject.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-04 加班申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void overApprovalPost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--加班申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 9参数
             */
            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.OVERAPPROVALPOST,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-05 调休申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void takeDaysOffPost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--调休申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.TAKEDAYSOFFPOST,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-06 借阅申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void borrowPost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--借阅--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.BORROWPOST,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-07 调薪申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void changeSalary(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--调薪--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.CHANGESALARY,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-08 用车申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void vehiclePost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--用车申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 参数
             */
            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.VEHICLEPOST,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-09 车辆维护申请 （obj形式上传）
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void maintenancePost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--车辆维护申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 12参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.MAINTENANCE,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 05-10 报销申请 （obj形式上传）
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void LRApplicationPost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", "UserHelper--报销申请--StoreID=" + UserHelper.getCurrentUser().getStoreID());
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", mCurrentUser.getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.LRAPPLICATIONPOST,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }


    /**
     * 06 申请详情 一下方法
     * <p>
     * 注：此处最好使用泛型，但是时间和个人能力原因，写出来有bug,就先用多个方法代替,
     * 附上泛型需要的如下2方法（未使用）
     */
    Class<T> clz;

    public UserHelper(Class<T> clz) {
        this.clz = clz;
    }

    public T applicationDetailPost(Context context, String ApplicationID, String ApplicationType, String StoreID, String EmployeeID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", StoreID)
                            .add("EmployeeID", EmployeeID));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), clz);
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 招聘详情 06-01
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static RecruitmentModel applicationDetailPostRecruitment(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RecruitmentModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 离职详情 06-02
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static DismissionModel applicationDetailPostDismission(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<DismissionModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 请假详情 06-03
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static LeaveModel applicationDetailPostLeave(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<LeaveModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 加班详情 06-04
     *
     * @param context
     * @param ApplicationID
     * @return
     * @throws MyException
     */
    public static WorkOverTimeModel applicationDetailPostWorkOverTime(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<WorkOverTimeModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 调休详情 06-05
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static TakeDaysOffModel applicationDetailPostTakeDaysOff(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));
            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<TakeDaysOffModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 申请 借阅详情 06-06
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType

     * @return
     * @throws MyException
     */
    public static BorrowModel applicationDetailPostBorrow(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<BorrowModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }


    /**
     * 调薪详情 06-07
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static SalaryAjustModel applicationDetailPostSalaryAdjust(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<SalaryAjustModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 用车详情 06-08
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static VehicleModel applicationDetailPostVehicle(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 车辆维护详情 06-09
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static VehicleMaintainModel applicationDetailPostVehicleMaintenance(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleMaintainModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 报销详情 06-10
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @param StoreID
     * @param EmployeeID
     * @return
     * @throws MyException
     */
    public static LoanReimbursementModel applicationDetailPostLoan(Context context, String ApplicationID, String ApplicationType, String StoreID, String EmployeeID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", StoreID)
                            .add("EmployeeID", EmployeeID));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<LoanReimbursementModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 调动详情 06-11
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static PositionReplaceModel applicationDetailPostPositionReplace(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<PositionReplaceModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 采购详情 06-12
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static ProcurementModel applicationDetailPostProcurement(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ProcurementModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 通知公告详情 06-13
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static NotificationAndNoticeModel applicationDetailPostNotificationAndNotice(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<NotificationAndNoticeModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 办公室详情 06-14
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static OfficeModel applicationDetailPostOffice(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<OfficeModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 领用详情 06-15
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static ReceiveModel applicationDetailPostReceive(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ReceiveModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 文件合同详情 06-16
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static ContractFileModel applicationDetailPostContractFile(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ContractFileModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 外出详情 06-16
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static OutGoingModel applicationDetailPostOutGoing(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<OutGoingModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 复试详情 06-18
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static RetestModel applicationDetailPostRetest(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RetestModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 会议详情 06-19
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static ConferenceModel applicationDetailPostConference(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.APPLICATIONDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));


            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ConferenceModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 07 审批详情
     * <p>
     * 注：此处最好使用泛型，但是时间和个人能力原因，写出来有bug,就先用多个方法代替,
     * 附上泛型需要的如下2方法（未使用）
     */

    public T approvalDetailPost(Context context, String ApplicationID, String ApplicationType, String StoreID, String EmployeeID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", StoreID)
                            .add("EmployeeID", EmployeeID));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), clz);
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 招聘详情 07-01
     */
    public static RecruitmentApvlModel approvalDetailPostRecruitment(Context context,
                                                                     String ApplicationID,
                                                                     String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RecruitmentApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 离职详情 07-02
     */
    public static DismissionApvlModel approvalDetailPostDimission(Context context,
                                                                  String ApplicationID,
                                                                  String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<DismissionApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 请假详情 07-03
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static LeaveApvlModel approvalDetailPostLeave(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<LeaveApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 请假详情 07-03
     *
     * @param context
     * @param ApplicationID
     * @param ApplicationType
     * @return
     * @throws MyException
     */
    public static WorkOverTimeApvlModel approvalDetailPostWorkOverTime(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<WorkOverTimeApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 调休详情 07-05
     */
    public static TakeDaysOffApvlModel approvalDetailPostTakeDaysOff(Context context,
                                                                     String ApplicationID,
                                                                     String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<TakeDaysOffApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 借阅详情 07-06
     */
    public static BorrowApvlModel approvalDetailPostBorrow(Context context,
                                                           String ApplicationID,
                                                           String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<BorrowApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 调薪详情 07-07
     */
    public static SalaryAjustApvlModel approvalDetailPostSalaryajust(Context context,
                                                                     String ApplicationID,
                                                                     String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<SalaryAjustApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 用车详情 07-08
     */
    public static VehicleApvlModel approvalDetailPostVehicle(Context context,
                                                             String ApplicationID,
                                                             String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 车辆维护详情 07-09
     */
    public static VehicleMaintainApvlModel approvalDetailPostVehicleMaintain(Context context,
                                                                             String ApplicationID,
                                                                             String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleMaintainApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 借款报销详情 07-10
     */
    public static VehicleMaintainApvlModel approvalDetailPostVehicleloan(Context context,
                                                                         String ApplicationID,
                                                                         String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleMaintainApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 调动详情 07-11
     */
    public static void approvalDetailPostPositionReplace(Context context,
                                                         String ApplicationID,
                                                         String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            //            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<VehicleMaintainApvlModel>() {
            //            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 采购详情 07-12
     */
    public static ProcurementApvlModel approvalDetailPostProcurement(Context context,
                                                                     String ApplicationID,
                                                                     String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ProcurementApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 通知公告详情 07-13
     */
    public static NotificationAndNoticeApvlModel approvalDetailPostNotificationAndNotice(Context context,
                                                                                         String ApplicationID,
                                                                                         String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<NotificationAndNoticeApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 审批 办公室详情 07-14
     */
    public static OfficeApvlModel approvalDetailPostOffice(Context context,
                                                           String ApplicationID,
                                                           String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<OfficeApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 审批 领用详情 07-15
     */
    public static ReceiveApvlModel approvalDetailPostReceive(Context context,
                                                            String ApplicationID,
                                                            String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ReceiveApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 审批 合同文件详情 07-16
     */
    public static ContractFileApvlModel approvalDetailPostContractFile(Context context,
                                                                       String ApplicationID,
                                                                       String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ContractFileApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 审批 外出详情 07-17
     */
    public static OUtGoingApvlModel approvalDetailPostOutGoing(Context context,
                                                               String ApplicationID,
                                                               String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<OUtGoingApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * 审批 外出详情 07-18
     */
    public static RetestApvlModel approvalDetailPostRetest(Context context,
                                                           String ApplicationID,
                                                           String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RetestApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
  /**
     * 审批 会议详情 07-19
     */
    public static ConferenceApvlModel approvalDetailPostConference(Context context,
                                                                   String ApplicationID,
                                                                   String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", mCurrentUser.getStoreID())
                            .add("EmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<ConferenceApvlModel>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 通讯录01
     * <p>
     * 获取 公司-分公司 员工信息
     */
    public static List<ContactsSonCOModel> getCompanySonOfCO(Context context) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", mCurrentUser.getEmployeeID());
            HttpResult httpResult = APIUtils.postForObject(WebUrl.ContactsManager.GETCOMPANYSONOFCO,
                    HttpParameter.create().
                            add("sEmployeeID", mCurrentUser.getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsSonCOModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

    }

    /**
     * 通讯录02
     * <p>
     * 获取 分公司-部门 员工信息
     */
    public static List<ContactsDeptModel> getContractsDeptOfSonCO(Context context, String sStoreID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.ContactsManager.DEPTINFOBYSTOREID,
                    HttpParameter.create()
                            .add("sStoreID", sStoreID)
                            .add("sEmployeeID", mCurrentUser.getEmployeeID()));//分公司ID

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsDeptModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

    }

    /**
     * 通讯录03
     * <p>
     * 获取部门 员工信息接口
     * <p>
     * post
     */
    public static List<ContactsEmployeeModel> getContractsEmployeeOfDept(Context context, String sDeptID) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.ContactsManager.EMPLOYEEINFOBYDEPTID,
                    HttpParameter.create()
                            .add("sDeptID", sDeptID)
                            .add("sEmployeeID", mCurrentUser.getEmployeeID())
            );

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsEmployeeModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 通讯录03
     * <p>
     * 获取部门 员工信息接口
     * <p>
     * post
     */
    public static String changePassword(Context context, String oldpassword, String newpassword) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.UserManager.CHANGE_PASSWORD,
                    HttpParameter.create()
                            .add("oldpassword", oldpassword)
                            .add("newpassword", newpassword)
                            .add("UserName", mCurrentUser.getName())
                            .add("StoreUserId", mCurrentUser.getStoreUserId())
            );

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }

            return httpResult.Message;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }


}
