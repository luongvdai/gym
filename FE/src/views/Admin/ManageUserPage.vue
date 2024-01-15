<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý người dùng</h1>
        <div class="device-top-right">
          <div class="product-content-toolbar-left-btn" @click="showFilter()">
            <MButton
              :ButtonCss="'btn-button-filter'"
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
            :placeholder="'Tìm kiếm theo tên'"
            :iconsearch="'icon-search'"
          />
          <div v-if="roleValue == 'ADMIN'" @click="exportExcel()" class="excel">
            <i class="ti ti-file-export"></i>
          </div>
        </div>
      </div>
      <div class="device-center">
        <MTable
          @custom-open-dbclick="openPopup"
          @change-click="changeClick"
          :RecordsLoad="UserTable"
          @data-load-delete="loadData"
          @get-List-Checkbox="getListEmployee"
          @Show-Img="openImg"
          :closeSelectedAll="closeSelectedAll"
          :thListTable="thList"
          :tdListTable="tdList"
          :objectTdList="objectTdList"
          :PopupNotilable="'Người dùng'"
          :baseURL="'manage/users/'"
          :TableFun="TableFun"
        />
        <div class="device-bottom">
          <div class="button-add">
            <m-button
              :text="'Thêm mới'"
              class="button-add-btn"
              @click="openPopup()"
              :class="'content-top-btn-content'"
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
              >Tổng số người dùng
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <ManageUserPopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @close-popup-click="closePopup"
      :userSelected="this.User"
      :popupLabel="Popuplabel"
      @load-data="loadData"
      :listBranch="this.listBranch"
      :listRole="this.listRole"
    >
    </ManageUserPopup>
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
    <MPopupViewImgVue
      v-if="isOpenImg"
      :urlImg="urlImage"
      @close-popupimg-click="closePopupImg"
    />
    <MLoading v-if="LoadingShow" />
  </div>
</template>

<script>
import MTable from "@/components/MTable/MTable.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import ManageUserPopup from "@/components/MPopup/ManageUserPopup.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MFilter from "../../components/MFilter/MFilter.vue";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "UserPageManagement",
  components: {
    MTable,
    MLoading,
    MButton,
    MInputSearch,
    ManageUserPopup,
    MFilter,
    MPopupViewImgVue,
  },
  created() {
    // this.WhereValue = "";
    this.loadData();
    this.getListRole();
    
    this.loadGender();
    this.loadActive();
    this.loadVerify();
    this.loadAllRole();
    this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
    if(this.roleValue == "ADMIN"){
      this.getListBranch();
    }
  },
  mounted() {
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closePopup() {
      this.popupOpen = false;
    },
    closePopupImg() {
      this.isOpenImg = false;
      this.urlImage = "";
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
      this.popupOpen = false;
    },

    /**
     * Hàm kiểm tra lựa chọn table
     */
    openPopup(record, Mode) {
      try {
        this.detailFormMode = 1;
      this.Popuplabel = "Thêm ";

      if (record && Mode == 2) {
        this.getOne(record["id"], true, 0);
        this.detailFormMode = 2;
        this.Popuplabel = "Sửa ";
      } else if (record && Mode == 1) {
        this.getOne(record["id"], true, 0);
      } else {
        this.User = null;
        this.popupOpen = true;
      }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm chọn fun bên table
     */
    async changeClick(record, selectedit) {
      try {
        //chọn đổi trạng thái
      if (selectedit == 2) {
        await this.getOne(record["id"], false, 2);
      }
      //đặt làm mentor
      if (selectedit == 3) {
        await this.getOne(record["id"], false, 3);
      }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm load một user
     */
    async getOne(id, isPopup, isChange) {
      this.LoadingShow = true;
      try {
        fetch(`${apiConfig}/manage/users/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if(!data.id){
              this.ShowToast(3, data.message);
            }else{
              this.User = data;
            if (isPopup) {
              this.popupOpen = true;
            } else {
              this.updateUser(id, isChange);
            }
            }
            this.LoadingShow = false; //Đóng loading
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

    /***
     * Hàm cập nhật user
     */
    async updateUser(id, isChange) {
      if (isChange == 2) {
        this.User.isActive = !this.User.isActive;
        console.log(this.User);
      }
      if (isChange == 3) {
        this.User.roleId = "4";
      }
       if(!this.User.password){
        this.User.password = "123xx45yy";
      }
      try {
        fetch(
          `${apiConfig}/manage/users/` + id,
          await fetchOptions("PUT", {
            username: this.User.username,
            password: this.User.password,
            fullName: this.User.fullName,
            email: this.User.email,
            phoneNumber: this.User.phoneNumber,
            address: this.User.address,
            gender: this.User.gender,
            dateOfBirth: this.User.dateOfBirth,
            roleId: this.User.roleId,
            avatar: this.User.avatar,
            branchId: this.getBranch,
            isVerify: this.User.isVerify,
            isActive: this.User.isActive,
          })
        )
          .then((res) => res.json())
          .then(async (res) => {
            if (!res.id) {
              this.ShowToast(3, res.message);
            } else {
              this.loadData();
              this.ShowToast((this.ToastStatus = 2));
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

    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },
    // hàm filter danh sách

    /**
     * hàm load dữ liệu
     */
    async loadData() {
      this.popupOpen = false;
      var limit = 5;
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
      try {
        fetch(`${apiConfig}/manage/users/${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.UserTable = data.content; //lưu dữ liệu
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
     * Hàm lấy danh sách vai trò
     */
    async getListRole() {
      try {
        fetch(`${apiConfig}/roles/filter`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization:
              "Bearer " +
              JSON.parse(localStorage.getItem("userData")).access_token,
          },
        })
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listRole = data;
            if(this.roleValue != 'MANAGER' && this.roleValue != 'ADMIN'){
              this.listRole = this.listRole.filter(role => role.id !== 2);
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
     * Hàm lấy danh sách chi nhánh
     */
    async getListBranch() {
      try {
        fetch(`${apiConfig}/branches`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization:
              "Bearer " +
              JSON.parse(localStorage.getItem("userData")).access_token,
          },
        })
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.listBranch = data.content;
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
     * Hàm hiển thị filter
     */
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.keywordFilter = "";
      this.WhereValueCombobox = null;
    },

    /**
     * Hàm đóng filter khi click outside
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

    /***
     * Hàm binding dữ liệu lên filter
     */

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

    /**
     * Hàm lấy dữ liệu filter
     */
    getFilter(value, keyword) {
      try {
        if (value != null) {
          if (this.valueFilter == keyword) {
            this.keywordFilter = "&" + keyword + "=" + value;
          } else {
            this.valueFilter = keyword;
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

    /***
     * Hàm tìm kiếm combobox
     */
    searchCombobox(value) {
      this.WhereValue = value;
      this.loadData();
    },

    /**
     * Hàm binding filter gender
     */
    loadGender() {
      try {
        this.UserGender.push({ id: true, name: "Nam" });
        this.UserGender.push({ id: false, name: "Nữ" });
        this.BindingFilter(
          "name",
          "id",
          "Giới tính",
          this.UserGender,
          "gender",
          true
        );
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    /**
     * Hàm binding filter trạng thái
     */
    loadActive() {
      try {
        this.UserActive.push({ id: true, name: "Đang hoạt động" });
        this.UserActive.push({ id: false, name: "Ngưng hoạt động" });
        this.BindingFilter(
          "name",
          "id",
          "Trạng thái",
          this.UserActive,
          "isActive",
          true
        );
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },
    /**
     * Hàm binding trạng thái kích hoạt
     */
    loadVerify() {
      try {
        this.UserVerify.push({ id: true, name: "Có" });
        this.UserVerify.push({ id: false, name: "Không" });
        this.BindingFilter(
          "name",
          "id",
          "Kích hoạt",
          this.UserVerify,
          "isVerify",
          true
        );
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm lấy dữ liệu vai trò
     */
    loadAllRole() {
      try {
        fetch(`${apiConfig}/roles/filter`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization:
              "Bearer " +
              JSON.parse(localStorage.getItem("userData")).access_token,
          },
        })
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.BindingFilter(
              "position",
              "id",
              "Vai trò",
              data,
              "roleId",
              true
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
     * Hàm xuất excel
     */
    async exportExcel() {
      try {
        //hiển loading
        this.LoadingShow = true;
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.getBranch}&keyword=${where}&page=${
          this.pagein - 1
        }${this.keywordFilter}`;
        fetch(
          `${apiConfig}/manage/users/export-to-excel${filter}`,
          await fetchOptions("GET")
        ).then((t) => {
          return t
            .blob()
            .then((b) => {
              //tạo thẻ a
              var a = document.createElement("a");
              //lấy ra URL
              a.href = URL.createObjectURL(b);
              // Set attribute của thẻ a và tên của file excel
              a.setAttribute("download", "Danh_sach_nguoi_dung.xlsx");
              a.click();
              // Ẩn Loading
              this.LoadingShow = false;
            })
            .catch((res) => {
              console.log(res.message);
            this.ShowToast(3, "Lỗi máy chủ");
            });
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
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
      //số trang mặc định
      TotalCount: 1,
      totalItems: 0,
      detailFormMode: 1,
      pagein: 1,
      Popuplabel: "",
      buttonlabel: "",
      listRole: {},
      listBranch: {},
      User: null,
      UserTable: [],
      thList: [
        { style: "min-width: 170px;", label: "Tên đăng nhập" },
        { style: "min-width: 170px;", label: "Họ và tên" },
        { style: "min-width: 170px;", label: "Địa chỉ" },
        { style: "min-width: 170px;", label: "Email" },
        { style: "min-width: 150px;", label: "Ảnh đại diện" },
        { style: "min-width: 170px;", label: "Ngày sinh" },
        { style: "min-width: 130px;", label: "Giới tính" },
        { style: "min-width: 130px;", label: "Vai trò" },
        { style: "min-width: 130px;", label: "Chi nhánh" },
        { style: "min-width: 130px;", label: "Trạng thái" },
        { style: "min-width: 130px;", label: "Kích hoạt" },
      ],

      //lưu property trong table
      tdList: [
        { property: "username" },
        { property: "fullName" },
        { property: "address" },
        { property: "email" },
        { property: "avatar", fun: 70 },
        { property: "dateOfBirth", fun: 12 },
        { property: "gender", fun: 2 },
        { property: "role", fun: 5 },
        { property: "branch", fun: 5 },
        { property: "isActive", fun: 7 },
        { property: "isVerify", fun: 8 },
        { property: "id", style: "display: none" },
      ],
      TableFun: [
        { name: "Đổi trạng thái", id: 2 },
        { name: "Đặt làm Mentor", id: 3 },
      ],
      isShowFilter: false,
      listFilter: [],
      UserGender: [],
      UserActive: [],
      UserVerify: [],
      BindingFilterValue: [],
      keywordFilter: "",
      searchFilter: null,
      searchLabel: null,
      valueFilter: null,
      isOpenImg: false,
      urlImage: null,
      objectTdList: [{ value: "position" }, { value: "name" }],
      roleValue: null,

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
  height: 60%;
  display: grid;
  align-items: center;
  position: absolute;
  right: 0;
}
.total-label {
  font-weight: bold;
}
.device-top-right {
  position: absolute;
  right: 15px;
  display: flex;
  margin-top: 5px;
}
.product-content-toolbar-left-btn {
  width: 100px;
  height: 100%;
}
.excel {
  display: flex;
  width: 30px;
  height: 30px;
  font-size: 35px;
  margin-left: 10px;
  color: #bbbbbb;
}
</style>
