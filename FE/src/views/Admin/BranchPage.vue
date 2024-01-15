<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý chi nhánh</h1>
        <div class="device-top-right">
          <MInputSearch
            @InputWhere="getWhereValue"
            :placeholder="'Tìm theo tên, địa chỉ'"
            :iconsearch="'icon-search'"
          />
        </div>
      </div>
      <div class="device-center">
        <MTable
          @custom-open-dbclick="openPopup"
          :RecordsLoad="BranchesTable"
          @data-load-delete="loadData"
          @get-List-Checkbox="getListEmployee"
          @Show-Img="openImg"
          :closeSelectedAll="closeSelectedAll"
          :thListTable="thList"
          :tdListTable="tdList"
          :PopupNotilable="'Chi nhánh'"
          :baseURL="'branches/'"
          :TableFun="TableFun"
        />
        <div class="device-bottom">
          <div class="button-add">
            <m-button
              :text="'Thêm mới'"
              :class="'content-top-btn-content'"
              class="button-add-btn"
              @click="openPopup(null)"
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
              >Tổng số chi nhánh
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <BranchPopup
      v-if="popupOpen"
      @close-popup-click="closePopup"
      :Mode="detailFormMode"
      :popupLabel="Popuplabel"
      @load-data="loadData"
      :branchSelected="Branch"
    />
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
import MLoading from "@/components/MLoading/MLoading.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import BranchPopup from "@/components/MPopup/BranchPopup.vue";
import MButton from "@/components/MButton/MButton.vue";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "BranchPageManager",
  components: {
    MTable,
    BranchPopup,
    MButton,
    MInputSearch,
    MPopupViewImgVue,
    MLoading,
  },
  created() {
    this.loadData();
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
      this.urlImage = null;
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
      this.popupOpen = false;
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
          this.Branch = null;
          this.popupOpen = true;
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async getOne(id) {
      try {
        this.LoadingShow = true;
        fetch(`${apiConfig}/branches/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (!data.id) {
              this.ShowToast(3, data.message);
            } else {
              this.Branch = data;
              this.popupOpen = true;
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
      this.LoadingShow = false; //Đóng loading
    },
    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },

    /**
     * hàm load dữ liệu
     */
    async loadData() {
      try {
        var limit = this.LimitValue; //lưu số lượng bản ghi
        if (limit == null) {
          //nếu không có, mặc định là 10
          limit = 5;
        }
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}&size=${limit}&page=${
          this.pagein - 1
        }`;
        fetch(`${apiConfig}/branches${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.BranchesTable = data.content; //lưu dữ liệu
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
      pageSize: 3,
      pagein: 1,
      detailFormMode: 1,
      Popuplabel: "",
      buttonlabel: "",
      Branch: null,
      BranchesTable: null,
      thList: [
        { style: "min-width: 200px;", label: "Tên chi nhánh" },
        { style: "min-width: 200px;", label: "Địa chỉ" },
        { style: "width: 150px;", label: "Logo" },
        { style: "width: 150px;", label: "Banner" },
      ],

      //lưu property trong table
      tdList: [
        { property: "name" },
        { property: "address" },
        { property: "logoUrl", fun: 70 },
        { property: "backgroundUrl", fun: 70 },
        { property: "id", style: "display: none" },
      ],
      TableFun: [{ name: "Xóa", id: 1 }],
      isOpenImg: false,
      urlImage: null,
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
</style>
