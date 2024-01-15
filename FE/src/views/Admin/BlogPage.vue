<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý bài viết</h1>
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
            :placeholder="'Tìm theo tên, tác giả'"
            :iconsearch="'icon-search'"
          />
        </div>
      </div>
      <div class="device-center">
        <MTable
          @custom-open-dbclick="openPopup"
          :RecordsLoad="BlogsTable"
          @data-load-delete="loadData"
          @get-List-Checkbox="getListEmployee"
          :closeSelectedAll="closeSelectedAll"
          :thListTable="thList"
          :tdListTable="tdList"
          :objectTdList="objectTdList"
          :PopupNotilable="'Bài viết'"
          :baseURL="'blogs/'"
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
              >Tổng số bài viết
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <BlogPopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @load-data="loadData"
      :popupLabel="Popuplabel"
      @close-popup-click="closePopup"
      :blogSelected="Blog"
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
    <MLoading v-if="LoadingShow" />
  </div>
</template>

<script>
import MTable from "@/components/MTable/MTable.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MButton from "@/components/MButton/MButton.vue";
import MFilter from "@/components/MFilter/MFilter.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import BlogPopup from "@/components/MPopup/BlogPopup.vue";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "BlogPageManager",
  components: {
    MTable,
    BlogPopup,
    MButton,
    MInputSearch,
    MFilter,
    MLoading,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  created() {
    this.loadData();
    //this.loadBlogType();
    //this.loadUser();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closePopup() {
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
          this.Blog = null;
          this.popupOpen = true;
        }
      } catch (error) {
        console.log(error);
      }
    },

    async getOne(id) {
      try {
        this.LoadingShow = true;
        fetch(`${apiConfig}/blogs/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            
            if (data.id) {
              this.Blog = data;
              this.popupOpen = true;
            }else{
              this.ShowToast(3, data.message);
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

    async loadBlogType() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?keyword=${where}`;
        fetch(`${apiConfig}/blog-types` + filter, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("typeId");
            }
            this.BindingFilter(
              "name",
              "id",
              "Loại bài viết",
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
    async loadUser() {
      try {
        var where = this.WhereValueCombobox; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${this.getBranch}&keyword=${where}`;
        fetch(
          `${apiConfig}/blogs/user-by-role` + filter,
          await fetchOptions("GET")
        )
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (this.WhereValueCombobox == null) {
              this.DeleteFilter("createdBy");
            }
            this.BindingFilter(
              "fullName",
              "username",
              "Tác giả",
              data,
              "createdBy",
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
     * Hàm mở popup filter
     * Nguyễn Văn Cương 11/11/2022
     */
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.loadBlogType();
      this.loadUser();
      this.WhereValueCombobox = null;
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
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /***
     * Hàm thêm những filter
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

    getWhereValue(value) {
      this.WhereValue = value;
      this.pagein = 1;
      this.loadData();
    },
    searchCombobox(value) {
      this.WhereValueCombobox = value;
      this.loadBlogType();
      this.loadUser();
    },

    /**
     * hàm load dữ liệu
     */
    async loadData() {
      try {
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
        fetch(`${apiConfig}/blogs${filter}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            if (data.status && data.status != 200) {
              this.ShowToast(3, data.message);
            } else {
              this.BlogsTable = data.content; //lưu dữ liệu
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
      WhereValueCombobox: null,
      //số trang mặc định
      TotalCount: 1,
      totalItems: 0,
      pageSize: 3,
      pagein: 1,
      detailFormMode: 1,
      Popuplabel: "",
      buttonlabel: "",
      Blog: null,
      BlogsTable: null,
      thList: [
        {
          style: "min-width: 170px; width: 300px;overflow:hiden;",
          label: "Tiêu đề",
        },
        { style: "min-width: 170px; width: 250px;", label: "Link đính kèm" },
        { style: "min-width: 170px;", label: "Thể loại" },
        { style: "min-width: 170px;", label: "Tác giả" },
      ],

      //lưu property trong table
      tdList: [
        { property: "title" },
        { property: "attach_url" },
        { property: "type", fun: 5 },
        { property: "user", fun: 5 },
        { property: "id", style: "display: none" },
      ],
      TableFun: [{ name: "Xóa", id: 1 }],
      listFilter: [],
      //lưu giá trị của filter trạng thái
      valueFilter: null,
      //lưu giá trị biding hiển thị
      BindingFilterValue: [],
      //trạng thái mở filter
      isShowFilter: false,
      //lưu loại filter
      keywordFilter: "",
      searchFilter: null,
      searchLabel: null,
      objectTdList: [{ value: "name" }, { value: "fullName" }],
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
</style>
