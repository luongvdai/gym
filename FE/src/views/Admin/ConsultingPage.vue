<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý thắc mắc</h1>
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
            :placeholder="'Tìm theo tên, nội dung'"
            :iconsearch="'icon-search'"
          />
        </div>
      </div>
      <div class="device-center">
        <MTable
          @custom-open-dbclick="openPopup"
          @change-click="changeStatus"
          @handle-click-funtable="getConsultingById"
          :RecordsLoad="ConsultingTable"
          @data-load-delete="loadData"
          :thListTable="thList"
          :tdListTable="tdList"
          :objectTdList="objectTdList"
          :PopupNotilable="'Thắc mắc'"
          :baseURL="'guest-consultant/'"
          :TableFun="TableFun"
        />
        <div class="device-bottom">
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
              >Tổng số thắc mắc
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
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
    <MLoading v-if="LoadingShow" />
    <ConsultantInforPopup
      v-if="isShowConsultantPopup"
      :recordSelected="this.selectedConsulting"
      @close-popup-click="closeConsultantInforPopup"
    />
    <ConsultantInforPopup
      v-if="isShowConsultantPopup"
      :recordSelected="this.selectedConsulting"
      @close-popup-click="closeConsultantInforPopup"
    />
  </div>
</template>

<script>
import MTable from "@/components/MTable/MTable.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MFilter from "../../components/MFilter/MFilter.vue";
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
import { mapMutations } from "vuex";
import ConsultantInforPopup from "@/components/MPopup/ConsultantInforPopup.vue";
export default {
  name: "ConsultingPageManagement",
  components: {
    MTable,
    MLoading,
    MButton,
    MInputSearch,
    MFilter,
    ConsultantInforPopup,
  },
  created() {
    this.loadData();
    this.loadStatusFilter();
  },

  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  methods: {
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.WhereValueCombobox = null;
      this.keywordFilter = "";
      this.loadBranch();
    },
    async openPopup(record) {
      await this.getConsultingById(record.id, "showPopup");
    },

    async loadData() {
      var limit = 5;
      var where = this.WhereValue; //lưu dữ liệu tìm kiếm
      if (where == null) {
        //nếu không có, mặc định là rỗng
        where = "";
      }
      const filter = `?branch=${
        this.getBranch
      }&keyword=${where}&size=${limit}&page=${this.pagein - 1}${
        this.keywordFilter
      }`;
      try {
        fetch(
          `${apiConfig}/guest-consultant${filter}`,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.ConsultingTable = data.content; //lưu dữ liệu
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
    async getConsultingById(id, mode) {
      if (mode == "showPopup") {
        this.LoadingShow = true;
      }
      try {
        fetch(`${apiConfig}/guest-consultant/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if(!data.id){
              this.ShowToast(data.message);
            }else{
              this.selectedConsulting = data;
            this.LoadingShow = false;
            if (mode == "showPopup") {
              this.isShowConsultantPopup = !this.isShowConsultantPopup;
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

    async changeStatus(record) {
      try {
        const changedData = {
          branchId: this.selectedConsulting.branchId,
          email: this.selectedConsulting.email,
          name: this.selectedConsulting.name,
          note: this.selectedConsulting.note,
          phoneNumber: this.selectedConsulting.phoneNumber,
          status: !this.selectedConsulting.status,
        };
        fetch(`${apiConfig}/guest-consultant/${record.id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization:
              "Bearer " +
              JSON.parse(localStorage.getItem("userData")).access_token,
          },
          body: JSON.stringify(changedData),
        })
        .then((data) => data.json().catch(() => data))
          .then((response) => {
            if (response.id) {
              // Xử lý thành công
              this.ShowToast(1);
              this.loadData();
            } else if (response.status == 500) {
              this.ShowToast(3, "Lỗi máy chủ");
            } else {
              this.ShowToast(3, response.message);
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
    loadStatusFilter() {
      try {
        this.consultingStatus.push({ id: true, name: "Đã xử lí" });
        this.consultingStatus.push({ id: false, name: "Chưa xử lí" });
        this.BindingFilter(
          "name",
          "id",
          "Trạng thái",
          this.consultingStatus,
          "status",
          true
        );
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    async loadBranch() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
      if (where == null) {
        //nếu không có, mặc định là rỗng
        where = "";
      }
      const filter = `?keyword=${where}`;
      fetch(`${apiConfig}/branches` + filter, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then((data) => {
          if (this.WhereValueCombobox == null) {
            this.DeleteFilter("branch");
          }
          this.BindingFilter(
            "name",
            "name",
            "Chi nhánh",
            data.content,
            "branch",
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

    ShowToast(Tstatus, message) {
      //trường hợp toast cập nhật trạng thái thành công
      if (Tstatus == 1) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_sus,
          css: toast.Toastcss_sus,
          color: toast.ToastMess_color_sus,
          message: "Cập nhật thành công",
        };
      } else if (Tstatus == 2) {
        this.ToastInfor = {
          show: true,
          icon: toast.Toastcssicon_faild,
          css: toast.Toastcss_faild,
          color: toast.ToastMess_color_faild,
          message: "Cập nhật thất bại",
        };
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
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),
    closeConsultantInforPopup() {
      this.isShowConsultantPopup = !this.isShowConsultantPopup;
    },
    searchCombobox(value) {
      this.WhereValueCombobox = value;
      this.loadBranch();
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
      selectedConsulting: {},
      isShowFilter: false,
      isShowConsultantPopup: false,
      BindingFilterValue: [],
      ConsultingTable: [],
      consultingStatus: [],
      listFilter: [],
      WhereValue: null,
      WhereValueCombobox: null,
      keywordFilter: "",
      searchFilter: null,
      searchLabel: null,
      valueFilter: null,
      LoadingShow: false,
      pagein: 1,
      TotalCount: 1,
      totalItems: 0,
      ToastStatus: 1,
      ToastInfor: {
        show: true,
        icon: "",
        css: "",
        color: "",
        message: "",
      },
      thList: [
        { style: "min-width: 190px;", label: "Ngày tạo" },
        { style: "min-width: 190px;", label: "Họ và tên" },
        { style: "min-width: 190px;", label: "Email" },
        { style: "min-width: 190px;", label: "Chi nhánh quan tâm" },
        { style: "min-width: 190px;", label: "Trạng thái" },
      ],

      //lưu property trong table
      tdList: [
        { property: "createdDate", fun: 999 },
        { property: "name" },
        { property: "email" },
        { property: "branch", fun: 5 },
        { property: "status", fun: 10 },

        { property: "id", style: "display: none" },
      ],
      TableFun: [
        { name: "Xóa", id: 1 },
        { name: "Đổi trạng thái", id: 5 },
      ],
      objectTdList: [{ value: "name" }],
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
</style>
