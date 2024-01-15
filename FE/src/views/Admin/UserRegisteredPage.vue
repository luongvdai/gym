<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý học viên</h1>
        <div class="device-top-right">
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
              :PopupNotilable="'Học viên'"
              :baseURL="'order/'"
              :TableFun="null"
            />
            <div class="paging">
              <v-pagination
                :length="TotalCount"
                v-model="pagein"
                :total-visible="7"
                prev-icon="ti ti-chevron-left"
                next-icon="ti ti-chevron-right"
              ></v-pagination>
            </div>
          </div>
          <div class="table2">
            <MTable
              @custom-open-dbclick="openEditor"
              :RecordsLoad="Orderdetails"
              @data-load-delete="CoursesTable"
              @get-List-Checkbox="getListEmployee"
              :closeSelectedAll="closeSelectedAll"
              :thListTable="thListDetail"
              @change-click="ManagementOrder"
              :tdListTable="tdListDetail"
              :objectTdList="objectTdListDetails"
              :PopupNotilable="'Học viên'"
              :baseURL="'order/'"
              :TableFun="TableFun"
            />
            <div class="paging">
              <v-pagination
                :length="TotalCountTable2"
                v-model="pageinTable2"
                :total-visible="7"
                prev-icon="ti ti-chevron-left"
                next-icon="ti ti-chevron-right"
              ></v-pagination>
            </div>
          </div>
        </div>
        <div class="device-bottom">
          <div class="total">
            <label
              >Tổng số học viên
              <label class="total-label">{{ TotalCustomer }}</label></label
            >
            <label
              >Tổng số khóa học
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <!-- <InfoGymPopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @load-data="loadData"
      :popupLabel="Popuplabel"
      @close-popup-click="closePopup"
      :branchSelected="userSelected"
    /> -->
    <ListGymInforPopup
      v-if="popupOpen"
      @close-popup-click="closePopup"
      :UserId="this.UserId"
      :UserCourseId="UserCourseId"
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
        :record="'đơn hàng'"
        @popup-ask-cance="ClosePopupAsk"
        @agree-delete-click="ManagementNoti"
        @close-notification-click="ClosePopupAsk"
        :getRecordCode="OrderFun"
        :MPopupN="NotificationMode"
      />
    </div>
    <MLoading v-if="LoadingShow" />
  </div>
</template>

<script>
import MTable from "@/components/MTable/MTable.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MPopupNotification from "@/components/MPopupNotification/MPopupNotification.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import MFilter from "@/components/MFilter/MFilter.vue";
import ListGymInforPopup from "@/components/MPopup/ListGymInforPopup.vue";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
  SET_CHAT_ISOPENED,
} from "@/stores/modules/storeconstants";
export default {
  name: "CoursePageManager",
  components: {
    MTable,
    MInputSearch,
    MFilter,
    MPopupNotification,
    MLoading,
    ListGymInforPopup,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  created() {
    (async () => {
      await this.loadData();
    })();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
      showChat: SET_CHAT_ISOPENED,
    }),
    closePopup() {
      this.popupOpen = false;
    },
    async ManagementOrder(record, select) {
      try {
        this.OrderId = record["id"];
        if (select == 10) {
          await this.showChat({
            show: false,
          });
          await this.showChat({
            show: true,
            receivedId: record["userid"],
          });
        } else if (select == 2) {
          this.isShowAskDelete = true;
          this.OrderFun = "cho phép học viên hoàn thành khóa học";
          this.UserCourseStatus = "COMPLETE_COURSE";
        } else {
          this.isShowAskDelete = true;
          this.OrderFun = "hủy khóa học cho học viên";
          this.UserCourseStatus = "OUT_COURSE";
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    ManagementNoti() {
      this.changeStatusUserCourse(this.OrderId, this.UserCourseStatus);
    },

    async changeStatusUserCourse(id, status) {
      try {
        await fetch(
          `${apiConfig}/user-courses/by-mentor`,
          await fetchOptions("PUT", {
            status: status,
            userCourseId: id,
          })
        )
          .then((res) => res.json())
          .then(async (res) => {
            if (!res.id) {
              this.ShowToast(3, res.message);
              if (res.status == 400) {
                this.isShowAskDelete = true;
                this.NotificationMode = 4;
              }
            } else {
              this.isShowAskDelete = false;
              this.ShowToast(2);
            }
            this.getOne(this.CourseIdSeleted);
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
              this.ShowToast(3, res.message);
              if (res.status == 400) {
                this.isShowAskDelete = true;
                this.NotificationMode = 4;
              }
            } else {
              this.isShowAskDelete = false;
              this.loadData();
              this.ShowToast(2);
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
          this.Order = null;
          this.popupOpen = true;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async openEditor(record, Mode) {
      try {
        this.detailFormMode = 1;
        this.Popuplabel = "Thêm ";

        if (record && Mode == 2) {
          console.log(record);
          await this.getUserInfor(record["userid"]);
          this.UserCourseId = record["id"];
          this.UserId = record["userid"];
          this.detailFormMode = 2;
          this.Popuplabel = "Sửa ";
        } else if (record && Mode == 1) {
          this.getUser(record["id"]);
        } else {
          this.Order = null;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async getUser(id) {
      try {
        await fetch(
          `${apiConfig}/user-courses/${id}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            if (!data.id) {
              this.ShowToast(3, data.message);
            } else {
              this.userId = data.userId;
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
    async getListGymInfor() {
      this.LoadingShow = true;
      const filter = `?size=5&page=${this.pagein - 1}`;
      try {
        fetch(
          `${apiConfig}/gym-user-information${filter}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (!data.id) {
              this.ShowToast(3, data.message);
            } else {
              this.InforTable = data.content;
              this.pageSize = data.totalPages;
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
    async getUserInfor(id) {
      try {
        this.LoadingShow = true;
        await fetch(
          `${apiConfig}/gym-information?userId=${id}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then(async (data) => {
            this.InforTable = data.content;
              this.popupOpen = true;
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
    async loadCourseType() {
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}`;
        fetch(`${apiConfig}/course-types/` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.DeleteFilter("typeId");
            this.BindingFilter(
              "name",
              "id",
              "Loại khóa tập",
              data,
              "typeId",
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

    BindingFilter(label, value, labelModel, filterModel, keyword, readonly) {
      try {
        if (!this.WhereValue && this.WhereValue != "") {
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
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    async getOne(id) {
      try {
        this.LoadingShow = true;
        this.Orderdetails = [];
        fetch(
          `${apiConfig}/user-courses/by-mentor?courseId=${id}&page=${
            this.pagein - 1
          }`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.Order = data.content;
              this.TotalCustomer = data.totalPages;
              this.getOrderDetails(data.content);
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
    getOrderDetails(orderDetails) {
      try {
        orderDetails.forEach((i) => {
          this.Orderdetails.push({
            fullName: i.user.fullName,
            phoneNumber: i.user.phoneNumber,
            address: i.user.address,
            slot: i.slot,
            status: i.status,
            userid: i.userId,
            id: i.id,
          });
        });
        this.LoadingShow = false; //Đóng loading
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
    },

    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },
    async searchCombobox(value) {
      this.WhereValue = value;
      await this.loadCourseType();
    },
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.loadCourseType();
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
        console.log(error);
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
        const filter = `?keyword=${where}&size=${limit}&page=${
          this.pagein - 1
        }${this.keywordFilter}`;
        fetch(
          `${apiConfig}/pt-courses/my-courses${filter}`,
          await fetchOptions("GET")
        )
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
          message: "Thêm thành công",
        };

        //trường hợp toast cập nhật thành công
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Sửa thành công",
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
    },
    pageinTable2() {
      this.getOne();
    },
    getBranch() {
      this.loadData();
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
      //gọi màn hình loadind
      LoadingShow: false,
      //lưu giá trị số lượng trang
      LimitValue: null,
      //lưu giá trị bản ghi hiện tại
      OffSetValue: null,
      //lưu giá trị tìm kiếm
      WhereValue: null,
      isShowFilter: false,
      //số trang mặc định
      TotalCount: 1,
      TotalCountTable2: 1,
      TotalCustomer: 0,
      totalItems: 0,
      pageSize: 3,
      pagein: 1,
      pageinTable2: 1,
      detailFormMode: 1,
      Popuplabel: "",
      buttonlabel: "",
      Order: null,
      Orderdetails: [],
      CoursesTable: [],
      thList: [
        { style: "min-width: 150px;", label: "Tiêu đề khóa tập" },
        { style: "min-width: 150px;", label: "Thời lượng" },
        { style: "min-width: 100px;", label: "Giá tiền" },
      ],
      //lưu property trong table
      tdList: [
        { property: "title" },
        { property: "rangeProcess", fun: 4 },
        { property: "price", fun: 6 },
        { property: "id", style: "display: none" },
      ],
      TableFun: [
        { name: "Hoàn thành khóa", id: 2 },
        { name: "Ra khỏi lớp", id: 3 },
        { name: "Nhắn tin trực tiếp", id: 10 },
      ],
      thListDetail: [
        { style: "min-width: 150px;", label: "Tên học viên" },
        { style: "min-width: 100px;", label: "Số điện thoại" },
        { style: "min-width: 100px;", label: "Địa chỉ" },
        { style: "min-width: 100px;", label: "SLOT" },
        { style: "min-width: 150px;", label: "Trạng thái" },
      ],
      //lưu property trong table
      tdListDetail: [
        { property: "fullName" },
        { property: "phoneNumber" },
        { property: "address" },
        { property: "slot" },
        { property: "status", fun: 11 },
        { property: "id", style: "display: none" },
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
      UserCourseStatus: null,
      NotificationMode: 3,
      userSelected: {},
      userId: null,
      CourseIdSeleted: null,
      InforTable: {},
      UserId: null,
      UserCourseId: null,
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
  width: 90%;
  height: 100%;
  position: relative;
  margin: 0 3%;
  padding: 0 3%;
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
  height: 110%;
}
.table2 {
  margin-left: 10px;
  height: 110%;
}
.double-table {
  height: 90%;
  display: flex;
}

.button-add {
  display: flex;
  margin-top: 10px;
  width: 30%;
}
.paging {
  width: 100%;
  height: 80px;
  position: relative;
  z-index: 10;
}
.total {
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
