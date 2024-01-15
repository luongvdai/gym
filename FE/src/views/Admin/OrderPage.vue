<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý đăng kí khóa tập</h1>
        <div class="device-top-right">
          <div class="product-content-toolbar-left-btn" @click="showFilter(1)">
            <MButton
              :ButtonCss="'btn-button-filter me-4'"
              :iconcss="'icon-filter'"
              :text="'Lọc'"
            />
          </div>
          <div
            class="toolbar-filter-label"
            v-for="(item, index) in BindingFilterValue"
            :key="item"
          >
            <label class="label-filter">{{ item.label }}</label>
            <div
              class="label-icon"
              @click="CancelFilterlabel(item, index)"
            ></div>
          </div>
          <div class="toolbar-filter-label">
            <label
              class="label-filter delete"
              @click="StartLoad"
              v-if="BindingFilterValue.length > 0"
              >Xóa điều kiện lọc</label
            >
          </div>
          <MInputSearch
            @InputWhere="getWhereValue"
            :placeholder="'Tìm theo tên'"
            :iconsearch="'icon-search'"
          />
        </div>
      </div>
      <div class="device-center">
        <div class="double-table">
          <div class="table1">
            <MTable
              @custom-open-dbclick="openPopup"
              :RecordsLoad="CoursesTable"
              @data-load-delete="CoursesTable"
              @get-List-Checkbox="getListEmployee"
              @change-click="OpenAsk"
              :closeSelectedAll="closeSelectedAll"
              :thListTable="thList"
              :tdListTable="tdList"
              :objectTdList="objectTdList"
              :PopupNotilable="'Đăng ký'"
              :baseURL="'order/'"
              :TableFun="TableFun"
            />
          </div>
          <div class="table2">
            <MTable
              @custom-open-dbclick="openEditDetails"
              :RecordsLoad="Orderdetails"
              @data-load-delete="CoursesTable"
              @get-List-Checkbox="getListEmployee"
              :closeSelectedAll="closeSelectedAll"
              :thListTable="thListDetail"
              :tdListTable="tdListDetail"
              :objectTdList="objectTdListDetails"
              :PopupNotilable="'Đăng ký'"
              :baseURL="'order/'"
              :TableFun="null"
            />
          </div>
        </div>
        <div class="device-bottom">
          <div class="button-add">
            <m-button
              :text="'Thêm mới'"
              class="button-add-btn"
              @click="openPopup(null)"
              :ButtonCss="'content-top-btn-content'"
            ></m-button>
          </div>
          <div class="paging">
            <v-pagination
              :length="TotalCount"
              v-model="pagein"
              :total-visible="7"
              prev-icon="ti ti-chevron-left"
              next-icon="ti ti-chevron-right"
            ></v-pagination>
          </div>
          <div class="total">
            <label
              >Tổng số trang
              <label class="total-label">{{ TotalCount }}</label></label
            >
            <label
              >Tổng số đăng ký
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <OrderDetailPopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @load-data="loadData"
      :popupLabel="Popuplabel"
      @close-popup-click="closePopup"
      :courseSelected="Course"
      :selectedBranch="getBranch"
    />
    <OrderPopup
      v-if="popupDetails"
      :Mode="detailFormMode"
      @load-data="getOne"
      @close-popup-click="closePopupDetails"
      :detailSelected="selectOrderDetails"
      :selectedBranch="getBranch"
    />
    <div ref="filter">
      <MFilter
        :typeInput="InputType"
        :filterList="listFilter"
        :searchFilter="searchFilter"
        :searchLabel="searchLabel"
        v-if="isShowFilter"
        @start-Search="searchCombobox"
        @get-Filter="getFilter"
        @start-Filter="loadData"
        :FilterStyle="StyleFilter"
        @Close-Filter="closeFilter"
      />
    </div>
    <div class="mpopup-ask">
      <MPopupNotification
        v-if="isShowAskDelete"
        :record="'đăng ký'"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="ManagementOrder"
        @close-notification-click="ClosePopupAsk"
        :errors="Requesterror"
        :getRecordCode="OrderFun"
        :MPopupN="NotificationMode"
      />
    </div>
    <div class="request">
      <MPopupRequies
        v-if="isShowRequest"
        @close-request-click="CloseRequest"
        @popup-ask-cance="CloseRequest"
        @agree-request="starRequest"
      />
    </div>
    <MLoading v-if="LoadingShow" />
  </div>
</template>

<script>
import MTable from "@/components/MTable/MTable.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MPopupRequies from "@/components/MPopupEdit/MPopupRequies.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import OrderDetailPopup from "@/components/MPopup/OrderDetailPopup.vue";
import OrderPopup from "@/components/MPopup/OrderPopup.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import MFilter from "@/components/MFilter/MFilter.vue";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";

export default {
  name: "CoursePageManager",
  components: {
    MTable,
    MButton,
    MInputSearch,
    MFilter,
    MPopupNotification,
    MPopupRequies,
    OrderDetailPopup,
    OrderPopup,
    MLoading,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  created() {
    (async () => {
      await this.loadData();
      this.loadStatus();
    })();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    closePopup() {
      this.popupOpen = false;
    },
    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    CloseRequest() {
      this.isShowRequest = false; //đóng popup hỏi người dùng
    },

    /**
     * Hàm kiểm tra lựa chọn từ table
     */
    OpenAsk(record, select) {
      try {
        this.OrderId = record["id"];
        this.Funselect = select;
        this.NotificationMode = 3;
        if (select == 2) {
          this.isShowAskDelete = true;
          this.OrderFun = "chấp nhận";
        } else if (select == 3) {
          this.isShowAskDelete = true;
          this.OrderFun = "từ chối";
        } else {
          this.isShowRequest = true;
          this.AssignIds.push(record["id"]);
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm kiểm tra lựa chọn sửa đăng ký
     */
    ManagementOrder() {
      try {
        var id = this.OrderId;
        var select = this.Funselect;
        if (select == 2) {
          this.editOrder(id, true);
        } else if (select == 3) {
          this.editOrder(id, false);
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm chỉ định nhân viên
     */
    async starRequest(user) {
      try {
        fetch(
          `${apiConfig}/manage/orders/assign`,
          await fetchOptions("POST", {
            assignId: user.id,
            orderIds: this.AssignIds,
          })
        )
          .then((res) => res.json().catch(() => res))
          .then((res) => {
            if (res.status == 400) {
              this.isShowAskDelete = true;
              this.NotificationMode = 4;
              this.Requesterror = res.message;
              this.CloseRequest();
              this.ShowToast(3, "Chỉ định thất bại");
            } else {
              this.CloseRequest();
              this.ShowToast(1, "Chỉ định thành công");
              this.loadData();
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm đóng popup hỏi người dùng có xóa không
     */
    ClosePopupAsk() {
      this.isShowAskDelete = false; //đóng popup hỏi người dùng
    },

    /**
     * Hàm đóng popup sửa detail
     */
    closePopupDetails() {
      this.popupDetails = false; //đóng popup hỏi người dùng
    },

    /***
     * Hàm chỉnh sửa đăng ký
     */
    async editOrder(id, isConfirm) {
      try {
        await fetch(
          `${apiConfig}/manage/orders/action`,
          await fetchOptions("PUT", {
            isConfirm: isConfirm,
            orderId: id,
          })
        )
          .then((res) => res.json())
          .then(async (res) => {
            if (!res.id) {
              if (res.status == 400) {
                this.isShowAskDelete = true;
                this.NotificationMode = 4;
                this.Requesterror = res.message;
              }
              this.ShowToast(3, res.message);
            } else {
              this.isShowAskDelete = false;
              if (isConfirm == true) {
                this.ShowToast(2, "Phê duyệt thành công!");
              } else {
                this.ShowToast(2, "Từ chối thành công!");
              }
              this.loadData();
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm kiểm tra lựa chọn table
     */
    openPopup(record, Mode) {
      try {
        this.detailFormMode = 1;
        this.Popuplabel = "Thêm ";

        if (record && Mode == 2) {
          this.getOne(record["id"]);
          this.detailFormMode = 2;
          this.Popuplabel = "Sửa ";
        } else if (record && Mode == 1) {
          this.getOne(record["id"]);
        } else {
          this.user = null;
          this.popupOpen = true;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm mở popup đăng ký chi tiết
     */
    openEditDetails(record) {
      this.selectOrderDetails = record["details"];
      this.popupDetails = true;
    },

    /**
     * Hàm load các trạng thái đăng ký
     */
    loadStatus() {
      this.ItemStatus.push({ id: "COMPLETE", name: "Hoàn thành" });
      this.ItemStatus.push({ id: "IN_PROGRESS", name: "Đang duyệt" });
      this.ItemStatus.push({ id: "PENDING", name: "Chưa thanh toán" });
      this.ItemStatus.push({ id: "REJECT", name: "Đã hủy" });
      this.BindingFilter(
        "name",
        "id",
        "Trạng thái",
        this.ItemStatus,
        "status",
        true
      );
    },

    /**
     * Hàm load danh sách người mua
     */
    async loadBuyer() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.getBranch}&keyword=${where}&roleId=5`;
        fetch(`${apiConfig}/manage/users` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("buyer");
            }
            this.BindingFilter(
              "fullName",
              "username",
              "Người đặt",
              data.content,
              "buyer",
              false
            );
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm load nhân viên được chỉ định
     */
    async loadAssign() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.getBranch}&keyword=${where}`;
        fetch(
          `${apiConfig}/manage/orders/user-assign` + filter,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("assignId");
            }
            this.BindingFilter(
              "fullName",
              "id",
              "Người được chỉ định",
              data,
              "assignId",
              false
            );
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm load những người phê duyệt
     */
    async loadManage() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.getBranch}&keyword=${where}`;
        fetch(
          `${apiConfig}/manage/orders/user-manage` + filter,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("approveId");
            }
            this.BindingFilter(
              "fullName",
              "id",
              "Người xác nhận",
              data,
              "approveId",
              false
            );
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
    Hàm xóa item trong filter trước khi Binding cái mới
     */
    DeleteFilter(keyword) {
      try {
        if (this.listFilter.length > 0) {
          for (let i = 0; i < this.listFilter.length; i++) {
            //tìm xem keyword trùng với filter nào k
            if (this.listFilter[i].keyword == keyword) {
              //xóa filter đó bằng index trong mảng
              this.listFilter.splice(i, 1);
            }
          }
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm binding dữ liệu lên filter
     */
    BindingFilter(label, value, labelModel, filterModel, keyword, readonly) {
      try {
        if (!this.WhereValueCombobox && this.WhereValueCombobox != "") {
          this.listFilter.push({
            label: label,
            value: value,
            filterlabel: labelModel,
            model: filterModel,
            keyword: keyword,
            readonly: readonly,
          });
        } else {
          this.searchFilter = filterModel;
          this.searchLabel = labelModel;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm lấy những lựa chọn filter
     */
    getFilter(value, keyword) {
      try {
        if (value != null) {
          if (this.valueFilter == value) {
            this.keywordFilter = "&" + keyword + "=" + value;
          } else {
            this.valueFilter = value;
            this.keywordFilter =
              this.keywordFilter + "&" + keyword + "=" + value;
          }
        } else {
          this.keywordFilter = "";
          this.isShowFilter = false;
        }
        this.pagein = 1;
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm lấy một đăng ký
     */
    async getOne(id) {
      try {
        this.LoadingShow = true;
        this.Orderdetails = [];
        fetch(`${apiConfig}/manage/orders/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (!data.id) {
              this.ShowToast(data.message);
            } else {
              this.Order = data;
              if (this.Order) {
                this.getOrderDetails(data.orderDetails);
              }
              
            }
            this.LoadingShow = false;
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
    },

    /**
     * Hàm lấy chi tiết đăng ký
     */
    getOrderDetails(orderDetails) {
      try {
        orderDetails.forEach((i) => {
          var PT = i.ptCourse.user;
          var course = i.ptCourse.course;
          this.Orderdetails.push({
            course: course,
            PT: PT,
            price: i.price,
            slot: i.slot,
            details: i,
          });
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
    },

    /**
     * Hàm search
     */
    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },

    /**
     * Hàm search trong combobox
     */
    async searchCombobox(value) {
      this.WhereValueCombobox = value;
      this.loadBuyer();
      this.loadAssign();
      this.loadManage();
    },

    /**
     * Hàm hiển thị filter
     */
    async showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.WhereValueCombobox = null;
      await this.loadBuyer();
      await this.loadAssign();
      await this.loadManage();
      this.keywordFilter = "";
    },
    /**
     * hàm click outsite
     * Nguyễn Văn Cương 01/10/2022
     */
    clickEventInterrupt(event) {
      try {
        if (this.isShowFilter == true) {
          //kiểm tra xem con chuột có click vào dropitem
          const isClick = this.$refs.filter.contains(event.target);
          if (!isClick) {
            this.isShowFilter = false;
          }
        }
      } catch (error) {
        console.log();
      }
    },
    /**
     * hàm load dữ liệu
     */
    async loadData() {
      try {
        var limit = this.LimitValue; //lưu số lượng bản ghi
        limit = 5;
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${
          this.getBranch
        }&keyword=${where}&size=${limit}&page=${this.pagein - 1}${
          this.keywordFilter
        }`;
        fetch(`${apiConfig}/manage/orders${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.CoursesTable = data.content; //lưu dữ liệu
              this.TotalCount = data.totalPages;
              this.totalItems = data.totalElements;
              this.isShowFilter = false;
            }
          })
          .catch((res) => {
            console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
    Hàm hiện thị thông báo
     */
    ShowToast(Tstatus, message) {
      //trường hợp toast thêm thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: message,
        };

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: message,
        };
        //trường hợp toast hành động thất bại
      } else {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: message,
        };
      }
      this.displayToast(this.ToastInfor);
    },
  },
  watch: {
    pagein() {
      this.loadData();
      this.Order = null;
    },
    getBranch() {
      this.loadData();
      this.Order = null;
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_SELECTED,
    }),
  },
  data() {
    return {
      popupOpen: false,
      popupDetails: false,
      //gọi màn hình loadind
      LoadingShow: false,
      //lưu giá trị số lượng trang
      LimitValue: null,
      //lưu giá trị bản ghi hiện tại
      OffSetValue: null,
      //lưu giá trị tìm kiếm
      WhereValue: null,
      WhereValueCombobox: null,
      isShowFilter: false,
      //số trang mặc định
      TotalCount: 1,
      totalItems: 0,
      pageSize: 3,
      pagein: 1,
      detailFormMode: 1,
      Popuplabel: "",
      buttonlabel: "",
      Order: null,
      Orderdetails: [],
      CoursesTable: [],
      selectOrderDetails: {},
      thList: [
        { style: "min-width: 150px;", label: "Ngày tạo" },
        { style: "min-width: 150px;", label: "Người đặt" },
        { style: "min-width: 150px;", label: "Người được chỉ định" },
        { style: "min-width: 150px;", label: "Người xác nhận" },
        { style: "min-width: 150px;", label: "Trạng thái đăng ký" },
      ],
      //lưu property trong table
      tdList: [
        { property: "createdDate", fun: 999 },
        { property: "user", fun: 5 },
        { property: "assignedPerson", fun: 5 },
        { property: "approvedBy", fun: 5 },
        { property: "status", fun: 9, class: "complete" },
      ],
      TableFun: [
        { name: "Chấp nhận", id: 2 },
        { name: "Từ chối", id: 3 },
        { name: "Chỉ định", id: 4 },
      ],
      thListDetail: [
        { style: "min-width: 150px;", label: "Tên khóa học" },
        { style: "min-width: 100px;", label: "PT" },
        { style: "min-width: 100px;", label: "Giá" },
        { style: "min-width: 100px;", label: "Slot" },
      ],
      //lưu property trong table
      tdListDetail: [
        { property: "course", fun: 5 },
        { property: "PT", fun: 5 },
        { property: "price", fun: 6 },
        { property: "slot" },
        { property: "details", style: "display: none" },
      ],
      listFilter: [],
      //lưu giá trị của filter trạng thái
      valueFilter: null,
      //lưu giá trị biding hiển thị
      BindingFilterValue: [],
      //lưu loại filter
      keywordFilter: "",
      ItemStatus: [],
      searchFilter: null,
      searchLabel: null,
      objectTdList: [{ value: "name" }, { value: "fullName" }],
      objectTdListDetails: [{ value: "username" }, { value: "title" }],
      isShowAskDelete: false,
      OrderId: null,
      Funselect: null,
      OrderFun: null,
      NotificationMode: 3,
      isShowRequest: false,
      AssignIds: [],
      Requesterror: null,
    };
  },
};
</script>

<style scoped>
.device-manager {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  opacity: 0;
  animation: fadeIn 1.5s ease-out forwards;
}
.device-container {
  width: 96%;
  height: 100%;
  position: relative;
  margin: 0%;
  padding: 0 1%;
  border: 1px solid rgb(216, 211, 211);
  border-radius: 13px;
  background: #fff;
}
.device-top {
  width: 100%;
  height: 12%;
  display: flex;
  align-items: center;
}
.device-center {
  width: 100%;
  height: 86%;
}
.device-bottom {
  margin-top: 10px;
  width: 100%;
  height: 16%;
  display: flex;
  position: relative;
}
.table1 {
  width: 65%;
  height: 110%;
}
.table2 {
  margin-left: 10px;
  width: 33%;
  height: 110%;
}
.double-table {
  width: 100%;
  height: 90%;
  display: flex;
}

.button-add {
  display: flex;
  margin-top: 10px;
  width: 30%;
}
.paging {
  width: 40%;
  height: 100%;
}
.total {
  width: 60;
  height: 60%;
  display: grid;
  align-items: center;
  position: absolute;
  right: 0;
}
.total-label {
  font-weight: bold;
}
.total-right {
  width: 40%;
  text-align: flex-end;
  font-size: 15px;
}
.device-top-right {
  position: absolute;
  right: 15px;
  display: flex;
  margin-top: 5px;
}
</style>
