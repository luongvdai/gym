<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý thiết bị</h1>
        <div class="device-top-right">
          <div class="product-content-toolbar-left-btn" @click="showFilter(1)">
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
            :placeholder="'Tìm theo tên thiết bị'"
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
          :RecordsLoad="DevicesTable"
          @data-load-delete="loadData"
          @get-List-Checkbox="getListEmployee"
          @Show-Img="openImg"
          :closeSelectedAll="closeSelectedAll"
          :thListTable="thList"
          :tdListTable="tdList"
          :PopupNotilable="'Thiết bị'"
          :baseURL="'devices/'"
          :objectTdList="objectTdList"
          :TableCheckBox="false"
          :TableFun="TableFun"
        />
        <div class="device-bottom">
          <div class="button-add">
            <m-button
              :text="'Thêm mới'"
              class="button-add-btn"
              :class="'content-top-btn-content'"
              @click="openPopup(null)"
            ></m-button>
          </div>
          <div class="paging text-center">
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
              >Tổng số thiết bị
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <DevicePopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @close-popup-click="closePopup"
      :deviceSelected="Device"
      :popupLabel="Popuplabel"
      @load-data="loadData"
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
import DevicePopup from "@/components/MPopup/DevicePopup.vue";
import MFilter from "@/components/MFilter/MFilter.vue";
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "DevicePageManager",
  components: {
    MTable,
    DevicePopup,
    MButton,
    MInputSearch,
    MFilter,
    MLoading,
    MPopupViewImgVue,
  },
  created() {
    this.loadData();
    //this.loadDeviceType();
    this.loadStatus();
    this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
  },
  mounted() {
    //gọi hàm clickoutsite
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

    searchCombobox(value) {
      this.WhereValueCombobox = value;
      this.loadDeviceType();
    },

    async loadDeviceType() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}`;
        fetch(`${apiConfig}/device-types` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("typeId");
            }
            this.BindingFilter(
              "name",
              "id",
              "Loại thiết bị",
              data.content,
              "typeId",
              false
            );
          })
          .catch((res) => {
            console.log(res);
          });
      } catch (error) {
        console.log(error);
      }
    },
    loadStatus() {
      this.ItemStatus.push({ id: true, name: "Đang sử dụng" });
      this.ItemStatus.push({ id: false, name: "Ngưng sử dụng" });
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
     * Hàm mở popup filter
     * Nguyễn Văn Cương 11/11/2022
     */
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.WhereValueCombobox = null;
      this.loadDeviceType();
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
      }
    },

    /***
     * Hàm thêm những filter
     */
    BindingFilter(label, value, labelModel, filterModel, keyword, readonly) {
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
    },

    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },

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
      }
    },

    openPopup(record, Mode) {
      try {
        this.detailFormMode = 1;
        this.Popuplabel = "Thêm ";

        if (record && Mode == 2) {
          this.getOne(record["id"], true);
          this.detailFormMode = 2;
          this.Popuplabel = "Sửa ";
        } else if (record && Mode == 1) {
          this.getOne(record["id"], true);
        } else {
          this.Device = null;
          this.popupOpen = true;
        }
      } catch (error) {
        console.log(error);
      }
    },

    /**
     * Hàm chọn fun bên table
     */
    async changeClick(device, selectedit) {
      try {
        //chọn đổi trạng thái
        if (selectedit == 2) {
          await this.getOne(device["id"], false);
        }
        //chọn nhân bản
        if (selectedit == 3) {
          await this.getOne(device["id"], true);
          this.detailFormMode = 1;
          this.Popuplabel = "Thêm ";
        }
      } catch (error) {
        console.log(error);
      }
    },

    async getOne(id, isPopup) {
      this.LoadingShow = true;
      try {
        fetch(`${apiConfig}/devices/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (!data.id) {
              this.ShowToast(3, data.message);
            }else{
              this.Device = data;
              if (isPopup) {
                this.popupOpen = true;
              } else {
                this.editDevice(id);
              }
            }
            this.LoadingShow = false; //Đóng loading
          })
          .catch((res) => {
            console.log(res);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
    },

    async editDevice(id) {
      var url = "devices/";
      try {
        await fetch(
          `${apiConfig}/` + url + id,
          await fetchOptions("PUT", {
            branchId: this.Device.branchId,
            name: this.Device.name,
            rangeMaintain: this.Device.rangeMaintain,
            imageUrl: this.Device.imageUrl,
            status: !this.Device.status,
            typeId: this.Device.typeId,
            price: this.Device.price,
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
            console.log(res);
            this.ShowToast(3, "Lỗi máy chủ");
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * hàm load dữ liệu
     */
    async loadData() {
      this.popupOpen = false;
      try {
        var limit = 5; //lưu số lượng bản ghi

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
        await fetch(`${apiConfig}/devices${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.DevicesTable = data.content; //lưu dữ liệu
              this.TotalCount = data.totalPages;
              this.totalItems = data.totalElements;
              this.isShowFilter = false;
            }
          })
          .catch((res) => {
            console.log(res);
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
    async exportExcel() {
      try {
        //hiển loading
        this.LoadingShow = true;

        fetch(
          `${apiConfig}/devices/export-to-excel`,
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
              a.setAttribute("download", "Danh_sach_thiet_bi.xlsx");
              a.click();
              // Ẩn Loading
              this.LoadingShow = false;
            })
            .catch((res) => {
              console.log(res);
              this.ShowToast(3, "Lỗi máy chủ");
            });
        });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.LoadingShow = false; //Đóng loading
    },
  },
  watch: {
    async pagein() {
      await this.loadData();
    },
    async getBranch() {
      await this.loadData();
    },
  },
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_SELECTED,
    }),
  },
  data() {
    return {
      //lưu css filter
      popupOpen: false,
      //gọi màn hình loadind
      LoadingShow: false,
      //lưu giá trị số lượng trang
      LimitValue: null,
      //lưu giá trị bản ghi hiện tại
      OffSetValue: null,
      //lưu giá trị tìm kiếm
      WhereValue: null,
      //lưu giá trị tìm kiếm bởi filer
      WhereValueCombobox: null,
      //số trang mặc định
      TotalCount: 1,
      totalItems: 0,
      pageSize: 3,
      detailFormMode: 1,
      pagein: 1,
      Popuplabel: "",
      buttonlabel: "",
      Device: null,
      DevicesTable: null,
      thList: [
        { style: "min-width: 170px;", label: "Tên thiết bị" },
        { style: "min-width: 170px;", label: "Thời hạn bảo dưỡng" },
        { style: "min-width: 170px;", label: "Giá" },
        { style: "min-width: 170px; ", label: "Ảnh thiết bị" },
        { style: "min-width: 170px;", label: "Người tạo" },
        { style: "min-width: 170px;", label: "Ngày tạo" },
        { style: "min-width: 170px;", label: "Trạng thái" },
      ],

      //lưu property trong table
      tdList: [
        { property: "name" },
        { property: "rangeMaintain", fun: 4 },
        { property: "price", fun: 6 },
        { property: "imageUrl", fun: 70 },
        { property: "user", fun: 5 },
        { property: "createdDate", fun: 999 },
        { property: "status", fun: 1 },
        { property: "typeId", style: "display: none" },
        { property: "id", style: "display: none" },
      ],
      TableFun: [
        { name: "Đổi trạng thái", id: 2 },
        { name: "Nhân bản", id: 3 },
      ],
      listFilter: [],
      //lưu giá trị của filter trạng thái
      valueFilter: null,
      //lưu giá trị biding hiển thị
      BindingFilterValue: [],
      //trạng thái mở filter
      isShowFilter: false,
      //lưu loại filter
      keywordFilter: "",
      ItemStatus: [],
      isOpenImg: false,
      urlImage: null,
      searchFilter: null,
      searchLabel: null,
      objectTdList: [{ value: "fullName" }],
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
