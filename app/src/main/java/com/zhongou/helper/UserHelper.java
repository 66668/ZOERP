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
import com.zhongou.model.ApprovalSModel;
import com.zhongou.model.ConferenceMSGModel;
import com.zhongou.model.ContactsDeptModel;
import com.zhongou.model.ContactsEmployeeModel;
import com.zhongou.model.ContactsSonCOModel;
import com.zhongou.model.MapAttendModel;
import com.zhongou.model.MyApplicationModel;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.MyCopyModel;
import com.zhongou.model.NoticeListModel;
import com.zhongou.model.NotificationListModel;
import com.zhongou.model.ProcurementListModel;
import com.zhongou.model.ReceiveListModel;
import com.zhongou.model.VehicleReturnModel;
import com.zhongou.model.VehicleReturnPostMaintenanceModel;
import com.zhongou.model.VehicleReturnPostUseModel;
import com.zhongou.model.applicationdetailmodel.FinancialAllModel;
import com.zhongou.utils.APIUtils;
import com.zhongou.utils.ConfigUtil;
import com.zhongou.utils.JSONUtils;
import com.zhongou.utils.Utils;
import com.zhongou.utils.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
     * 01-01
     * 获取 我的申请记录
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
                        .add("pageSize", "20")
                        .add("storeID", mCurrentUser.getStoreID())
                        .add("employeeId", mCurrentUser.getEmployeeID())
        );

        if (hr.hasError()) {
            throw hr.getError();
        }
        //方式一：直接调用gson
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApplicationModel>>() {
        //        }.getType());

        //方式二:utils工具类
        return (List<MyApplicationModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApplicationModel>>() {
        }.getType());
    }

    /**
     * 01-01-02 申请详情
     * <p>
     * 注：使用泛型
     */
    Class<T> clz;

    public UserHelper(Class<T> clz) {
        this.clz = clz;
    }

    public T applicationDetailPost(Context context, String ApplicationID, String ApplicationType) throws MyException {
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
            //方式一：
            //            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RecruitmentModel>() {
            //            }.getType());

            //方式二：泛型
            return (new Gson()).fromJson(httpResult.jsonObject.toString(), clz);

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-02 获取 我的审批记录
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
                            .add("storeID", UserHelper.getCurrentUser().getStoreID())
                            .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                            .add("pageSize", "20"));

            if (hr.hasError()) {
                throw hr.getError();
            }
            //方式一：直接用gson
            //            return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApprovalModel>>() {
            //            }.getType());

            //方式二：utils工具类
            return (List<MyApprovalModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<MyApprovalModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-02-02 审批详情
     * <p>
     * 使用泛型
     */

    public T approvalDetailPost(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETMYAPPROVALDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", UserHelper.getCurrentUser().getStoreID())
                            .add("EmployeeID", UserHelper.getCurrentUser().getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonObject.toString());
            //            return (new Gson()).fromJson(httpResult.jsonObject.toString(), new TypeToken<RecruitmentApvlModel>() {
            //            }.getType());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), clz);

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-02-02-01/02审批--同意/驳回接口
     *
     * @param context
     * @param sApprovalid
     * @param sComment
     * @param sIsend
     * @param sApplicationid
     * @param sYesorno
     * @return
     * @throws MyException
     */

    public static String agreeOrDisAgreeMyApproval(Context context, String sApprovalid, String sComment, String sIsend, String sApplicationid, String sYesorno) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            Log.d("SJY", mCurrentUser.getEmployeeID());
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.APPROVALE_AGREE_DISAGREE,
                    HttpParameter.create()
                            .add("sApprovalid", sApprovalid)
                            .add("sComment", sComment)
                            .add("sIsend", sIsend)
                            .add("sApplicationid", sApplicationid)
                            .add("sYesorno", sYesorno));

            if (hr.hasError()) {
                throw hr.getError();
            }

            return hr.Message;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-02-02-03审批-转交接口
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static String transfortoMyApproval(Context context, ApprovalSModel model) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        String toJsondata = new Gson().toJson(model);
        try {
            Log.d("SJY", mCurrentUser.getEmployeeID());
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.APPROVAL_TRANSFORTO,
                    HttpParameter.create().add("obj", toJsondata));

            if (hr.hasError()) {
                throw hr.getError();
            }

            return hr.Message;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-02-02-04审批-抄送接口
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static String CopyToMyApproval(Context context, ApprovalSModel model) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        String toJsondata = new Gson().toJson(model);
        try {
            Log.d("SJY", mCurrentUser.getEmployeeID());
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.APPROVAL_COPYTO,
                    HttpParameter.create().add("obj", toJsondata));

            if (hr.hasError()) {
                throw hr.getError();
            }

            return hr.Message;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }


    /**
     * 01-03 获取 我的抄送
     * <p></p>
     *
     * @param context
     * @param iMaxTime
     * @param iMinTime
     * @return
     * @throws MyException
     */

    public static List<MyCopyModel> getMyCopyList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        try {
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETCOPYLIST,
                    HttpParameter.create()
                            .add("iMaxTime", iMaxTime)
                            .add("iMinTime", iMinTime)
                            .add("storeID", UserHelper.getCurrentUser().getStoreID())
                            .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                            .add("pageSize", "20"));

            if (hr.hasError()) {
                throw hr.getError();
            }

            //方式一
            //            return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MyCopyModel>>() {
            //            }.getType());

            //方式二：
            return (List<MyCopyModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<MyCopyModel>>() {
            }.getType());
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 01-03-02抄送详情
     * <p>
     * 使用泛型
     */
    public T copyDetailPost(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.GETCOPYDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", UserHelper.getCurrentUser().getStoreID())
                            .add("EmployeeID", UserHelper.getCurrentUser().getEmployeeID()));

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
     * -01 招聘申请 （obj形式上传）
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
            /**
             * 参数保存成json
             */
            jsonObject.put("CreateTime", Utils.getCurrentTime());//申请时间
            jsonObject.put("AppEmployeeID", UserHelper.getCurrentUser().getEmployeeID());
            jsonObject.put("StoreID", UserHelper.getCurrentUser().getStoreID());

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
     * -02 离职申请 （obj形式上传）
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
            /**
             * 参数保存成json
             */
            jsonObject.put("CreateTime", Utils.getCurrentTime());
            jsonObject.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            jsonObject.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -03 请假申请 （obj形式上传）
     * <p></p>
     *
     * @param context
     * @param jsonObject
     * @throws MyException
     */
    public static void leavePost(Context context, JSONObject jsonObject, File picPath) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            /**
             * 参数保存成json
             */
            jsonObject.put("CreateTime", Utils.getCurrentTime());
            jsonObject.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            jsonObject.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.LEAVEPOST,
                    HttpParameter.create().add("obj", jsonObject.toString()), picPath);

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
     * -04 加班申请 （obj形式上传）
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
            /**
             * 参数保存成json 9参数
             */
            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -05 调休申请 （obj形式上传）
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
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -06 借阅申请 （obj形式上传）
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
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -07 调薪申请 （obj形式上传）
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
            /**
             * 参数保存成json 参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -08 用车申请 （obj形式上传）
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
            /**
             * 参数保存成json 参数
             */
            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -09 车辆维护申请 （obj形式上传）
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

            /**
             * 参数保存成json 12参数
             */

            js.put("CreateTime", Utils.getCurrentTime());
            js.put("StoreID", UserHelper.getCurrentUser().getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

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
     * -10 财务申请 （obj形式上传）
     * 借款 报销 费用申请 付款
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static String LRApplicationPost(Context context, JSONObject js, File file) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {

            /**
             * 参数保存成json 参数
             */

            //            js.put("CreateTime", Utils.getCurrentTime());
            //            js.put("StoreID", mCurrentUser.getStoreID());
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.LRAPPLICATIONPOST
                    , HttpParameter.create().add("obj", js.toString())
                    , file);

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }

            return httpResult.Message;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        return null;
    }


    /**
     * 03-01添加地图考勤,（obj形式上传)
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
            js.put("EmployeeID", UserHelper.getCurrentUser().getEmployeeID());
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
     * 03-02获取地图考勤记录
     *
     * @param context
     * @param iMaxTime
     * @param iMinTime
     * @return
     * @throws MyException
     */
    public static List<MapAttendModel> getMapAttendRecord(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETATTENDRECORD,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }
        //方式一：直接用gson
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<MapAttendModel>>() {
        //        }.getType());

        //方式二：
        return (List<MapAttendModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<MapAttendModel>>() {
        }.getType());

    }

    /**
     * 04 应用 公告
     * 列表
     */
    public static List<NoticeListModel> GetAppNoticeList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETNOTICELIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("pageSize", "20")
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
        );

        if (hr.hasError()) {
            throw hr.getError();
        }

        //方式一：
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<NoticeListModel>>() {
        //        }.getType());

        //方式二：
        return (List<NoticeListModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<NoticeListModel>>() {
        }.getType());

    }

    /**
     * 04-02
     * 进入公告详情，标记已读
     */
    public static void postReadThisNotice(Context context, String ApplicationID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        Log.d("SJY", "ApplicationID=" + ApplicationID + "--EmployeeID=" + UserHelper.getCurrentUser().getEmployeeID());
        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.READTHISNOTICE,
                HttpParameter.create()
                        .add("ApplicationID", ApplicationID)
                        .add("EmployeeID", UserHelper.getCurrentUser().getEmployeeID())
        );
        if (hr.hasError()) {
            throw hr.getError();
        }
    }

    /**
     * 05 应用 通知
     * 通知列表
     */
    public static List<NotificationListModel> GetAppNotificationList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.GETNOTIFICATIONLIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("pageSize", "20")
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
        );

        if (hr.hasError()) {
            throw hr.getError();
        }

        //方式一：
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<NotificationListModel>>() {
        //        }.getType());

        //方式二：
        return (List<NotificationListModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<NotificationListModel>>() {
        }.getType());
    }

    /**
     * 05-02
     * 进入公告详情，标记已读
     */
    public static void postReadThisNoti(Context context, String ApplicationID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        Log.d("SJY", "ApplicationID=" + ApplicationID + "--EmployeeID=" + UserHelper.getCurrentUser().getEmployeeID());
        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.READTHISNOTICE,
                HttpParameter.create()
                        .add("ApplicationID", ApplicationID)
                        .add("EmployeeID", UserHelper.getCurrentUser().getEmployeeID())
        );
        if (hr.hasError()) {
            throw hr.getError();
        }
    }

    /**
     * 06-01应用-财务记录
     */
    public static List<FinancialAllModel> GetAppFinanceList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.FINACELIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //方式一：
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<FinancialAllModel>>() {
        //        }.getType());


        //方式二：
        return (List<FinancialAllModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<FinancialAllModel>>() {
        }.getType());
    }

    /**
     * 07 会议
     */

    public static List<ConferenceMSGModel> GetAppConferenceList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.CONFERENCELIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<ProcurementListModel>>() {
        //        }.getType());

        //方式二：
        return (List<ConferenceMSGModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<ConferenceMSGModel>>() {
        }.getType());
    }

    /**
     * 08-01应用-采购记录
     */
    public static List<ProcurementListModel> GetAppProcurementList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.PROCUREMENTLIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<ProcurementListModel>>() {
        //        }.getType());

        //方式二：
        return (List<ProcurementListModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<ProcurementListModel>>() {
        }.getType());
    }

    /**
     * 08-02应用-领用记录
     */
    public static List<ReceiveListModel> GetAppReceiveList(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.RECEIVELIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //1
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<ReceiveListModel>>() {
        //        }.getType());

        //方式二：
        return (List<ReceiveListModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<ReceiveListModel>>() {
        }.getType());
    }

    /**
     * 09-01交车记录
     */
    public static List<VehicleReturnModel> GetVehicleReturnResults(Context context, String iMaxTime, String iMinTime) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.VEHICLERETURNLIST,
                HttpParameter.create()
                        .add("iMaxTime", iMaxTime)
                        .add("iMinTime", iMinTime)
                        .add("storeID", UserHelper.getCurrentUser().getStoreID())
                        .add("employeeId", UserHelper.getCurrentUser().getEmployeeID())
                        .add("pageSize", "20"));

        if (hr.hasError()) {
            throw hr.getError();
        }

        //1
        //        return (new Gson()).fromJson(hr.jsonArray.toString(), new TypeToken<List<VehicleReturnModel>>() {
        //        }.getType());

        //方式二：
        return (List<VehicleReturnModel>) JSONUtils.fromJson(hr.jsonArray.toString(), new TypeToken<List<VehicleReturnModel>>() {
        }.getType());
    }

    /**
     * 09-02交车详情
     * 泛型
     */
    public T getVehicleReturnDetail(Context context, String ApplicationID, String ApplicationType) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        try {

            HttpResult httpResult = APIUtils.postForObject(WebUrl.AppsManager.VEHICLERETURNDETAIL,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", UserHelper.getCurrentUser().getStoreID())
                            .add("EmployeeID", UserHelper.getCurrentUser().getEmployeeID()));
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
     * 09-03-01交车-用车提交(obj形式)
     */
    public static String postVehicleReturnUse(Context context, VehicleReturnPostUseModel model) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        String toJsondata = new Gson().toJson(model);
        Log.d("SJY", "转换成js=" + toJsondata);
        try {
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.VEHICLERETURNDPOST,
                    HttpParameter.create().add("obj", toJsondata));

            if (hr.hasError()) {
                throw hr.getError();
            }

            return hr.Message;
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }


    /**
     * 09-03-02交车-维保提交(obj形式)
     */
    public static String postVehicleReturnMaintenance(Context context, VehicleReturnPostMaintenanceModel model) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        model.setEmployeeID(UserHelper.getCurrentUser().getEmployeeID());
        String toJsondata = new Gson().toJson(model);
        Log.d("SJY", "转换成js=" + toJsondata);
        try {
            HttpResult hr = APIUtils.postForObject(WebUrl.AppsManager.VEHICLERETURNDPOST,
                    HttpParameter.create().add("obj", toJsondata));

            if (hr.hasError()) {
                throw hr.getError();
            }

            return hr.Message;
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
            Log.d("SJY", UserHelper.getCurrentUser().getEmployeeID());
            HttpResult httpResult = APIUtils.postForObject(WebUrl.ContactsManager.GETCOMPANYSONOFCO,
                    HttpParameter.create().
                            add("sEmployeeID", UserHelper.getCurrentUser().getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            //1
            //            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsSonCOModel>>() {
            //            }.getType());


            //方式二：
            return (List<ContactsSonCOModel>) JSONUtils.fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsSonCOModel>>() {
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
                            .add("sEmployeeID", UserHelper.getCurrentUser().getEmployeeID()));//分公司ID

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            //1
            //            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsDeptModel>>() {
            //            }.getType());


            //方式二：
            return (List<ContactsDeptModel>) JSONUtils.fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsDeptModel>>() {
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
                            .add("sEmployeeID", UserHelper.getCurrentUser().getEmployeeID())
            );

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            //
            //            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsEmployeeModel>>() {
            //            }.getType());

            //方式二：
            return (List<ContactsEmployeeModel>) JSONUtils.fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsEmployeeModel>>() {
            }.getType());

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

    /**
     * 通讯录 审批-选择审批人/转交
     * <p>
     * 获取级别权限的所有联系人
     */
    public static List<ContactsEmployeeModel> getContactsSelectCo(Context context) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.ContactsManager.CONTACTSSELECTCO,
                    HttpParameter.create().
                            add("sEmployeeID", getCurrentUser().getEmployeeID()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            Log.d("HTTP", httpResult.jsonArray.toString());

            //1
            //            return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsEmployeeModel>>() {
            //            }.getType());


            //方式二：
            return (List<ContactsEmployeeModel>) JSONUtils.fromJson(httpResult.jsonArray.toString(), new TypeToken<List<ContactsEmployeeModel>>() {
            }.getType());

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }

    }


    /**
     * 修改密码
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
                            .add("UserName", UserHelper.getCurrentUser().getName())
                            .add("StoreUserId", UserHelper.getCurrentUser().getStoreUserId())
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
