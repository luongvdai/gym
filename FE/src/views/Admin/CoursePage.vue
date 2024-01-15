<template>
  <div class="device-manager">
    <div class="device-container">
      <div class="device-top">
        <h1>Quản lý khóa tập</h1>
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
            :placeholder="'Tìm theo tên khóa tập'"
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
          :RecordsLoad="CoursesTable"
          @change-click="changeClick"
          @data-load-delete="CoursesTable"
          @get-List-Checkbox="getListEmployee"
          @Show-Img="openImg"
          :closeSelectedAll="closeSelectedAll"
          :thListTable="thList"
          :tdListTable="tdList"
          :objectTdList="'name'"
          :PopupNotilable="'Khóa tập'"
          :baseURL="'courses/'"
          :TableFun="TableFun"
        />
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
              >Tổng số khóa học
              <label class="total-label">{{ totalItems }}</label></label
            >
          </div>
        </div>
      </div>
    </div>
    <CoursePopup
      v-if="popupOpen"
      :Mode="detailFormMode"
      @load-data="loadData"
      :popupLabel="Popuplabel"
      @close-popup-click="closePopup"
      :courseSelected="Course"
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
import MPopupViewImgVue from "@/components/MPopupEdit/MPopupViewImg.vue";
import MLoading from "@/components/MLoading/MLoading.vue";
import MInputSearch from "@/components/MInput/MInputSearch.vue";
import MButton from "@/components/MButton/MButton.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import MFilter from "@/components/MFilter/MFilter.vue";
import CoursePopup from "@/components/MPopup/CoursePopup.vue";
import { GET_BRANCH_SELECTED } from "@/stores/modules/storeconstants";
import { mapGetters } from "vuex";
import toast from "@/resouce/toast";
import { mapMutations } from "vuex";
import {
  LOADING_SPINNER_SHOW_MUTATION,
  TOAST_SHOW_MUTATION,
} from "@/stores/modules/storeconstants";
export default {
  name: "CoursePageManager",
  components: {
    MTable,
    CoursePopup,
    MButton,
    MInputSearch,
    MFilter,
    MLoading,
    MPopupViewImgVue,
  },
  mounted() {
    //gọi hàm clickoutsite
    window.addEventListener("mousedown", this.clickEventInterrupt);
  },
  created() {
    (async () => {
      await this.loadData();
      this.loadStatus();
      this.loadPrice();
      this.loadRange();
      this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
    })();
  },
  methods: {
    ...mapMutations({
      displayToast: TOAST_SHOW_MUTATION,
      showLoading: LOADING_SPINNER_SHOW_MUTATION,
    }),

    closePopupImg() {
      this.isOpenImg = false;
      this.urlImage = "";
    },
    openImg(value) {
      this.urlImage = value;
      this.isOpenImg = true;
      this.popupOpen = false;
    },

    closePopup() {
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
          this.getOne(record["id"], true);
          this.detailFormMode = 2;
          this.Popuplabel = "Sửa ";
        } else if (record && Mode == 1) {
          this.getOne(record["id"], true);
        } else {
          this.Course = null;
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
    async changeClick(course, selectedit) {
      //chọn đổi trạng thái
      try {
        if (selectedit == 2) {
          await this.getOne(course["id"], false);
        }
        //chọn nhân bản
        if (selectedit == 3) {
          await this.getOne(course["id"], true);
          this.detailFormMode = 1;
          this.Popuplabel = "Thêm ";
        }
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
    },

    /**
     * Hàm load một khóa học
     */
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
              data.content,
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

    loadStatus() {
      this.ItemStatus.push({ id: true, name: "Đang hoạt động" });
      this.ItemStatus.push({ id: false, name: "Ngưng hoạt động" });
      this.BindingFilter(
        "name",
        "id",
        "Trạng thái",
        this.ItemStatus,
        "isActive",
        true
      );
    },

    loadPrice() {
      this.ItemPrice.push({ id: 1, name: "Dưới 500 nghìn VNĐ" });
      this.ItemPrice.push({ id: 3, name: "Dưới 1 triệu VNĐ" });
      this.ItemPrice.push({ id: 4, name: "Dưới 2 triệu VNĐ" });
      this.ItemPrice.push({ id: 5, name: "Hơn 2 triệu VNĐ" });
      this.BindingFilter(
        "name",
        "id",
        "Giá",
        this.ItemPrice,
        "rangePrice",
        true
      );
    },

    loadRange() {
      for (let i = 0; i <= 12; i++) {
        this.ItemRange.push({ id: i, name: i + " tháng" });
      }
      this.BindingFilter(
        "name",
        "id",
        "Thời lượng",
        this.ItemRange,
        "rangeProcess",
        true
      );
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
     * Hàm load một khóa học
     */
    async getOne(id, isPopup) {
      this.LoadingShow = true;
      try {
        fetch(`${apiConfig}/courses/${id}`, await fetchOptions("GET"))
          .then((data) => data.json().catch(() => data))
          .then((data) => {
            this.LoadingShow = false; //Đóng loading
            if (data.id) {
              this.Course = data;
              if (isPopup) {
                this.popupOpen = true;
              } else {
                this.editCourse(id);
              }
            } else {
              this.ShowToast(data.message);
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
     * Hàm chỉnh sửa khóa học
     */
    async editCourse(id) {
      var url = "courses/";
      try {
        await fetch(
          `${apiConfig}/` + url + id,
          await fetchOptions("PUT", {
            banner: this.Course.banner,
            branchId: this.Course.branchId,
            courseTypeId: this.Course.courseTypeId,
            imageUrl: this.Course.imageUrl,
            isActive: !this.Course.isActive,
            rangeProcess: this.Course.rangeProcess,
            title: this.Course.title,
            price: this.Course.price,
            description: this.Course.description,
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
    async searchCombobox(value) {
      this.WhereValue = value;
      await this.loadCourseType();
    },
    showFilter() {
      this.isShowFilter = !this.isShowFilter;
      this.WhereValueCombobox = null;
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
        this.ShowToast(3, error);
      }
    },
    /**
     * hàm load dữ liệu
     */
    async loadData() {
      try {
        var where = this.WhereValue; //lưu dữ liệu tìm kiếm
        if (where == null) {
          //nếu không có, mặc định là rỗng
          where = "";
        }
        const filter = `?branchId=${
          this.getBranch
        }&keyword=${where}&size=5&page=${this.pagein - 1}${this.keywordFilter}`;
        fetch(`${apiConfig}/courses${filter}`, await fetchOptions("GET"))
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
          `${apiConfig}/courses/export-to-excel${filter}`,
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
              a.setAttribute("download", "Danh_sach_khoa_hoc.xlsx");
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
      isShowFilter: false,
      //số trang mặc định
      TotalCount: 1,
      totalItems: 0,
      pageSize: 3,
      pagein: 1,
      detailFormMode: 1,
      Popuplabel: "",
      buttonlabel: "",
      Course: null,
      CoursesTable: [],
      thList: [
        { style: "min-width: 235px;", label: "Tiêu đề" },
        { style: "min-width: 235px;", label: "Giá tiền" },
        { style: "min-width: 235px;", label: "Thời lượng" },
        { style: "min-width: 235px;", label: "Ảnh khóa tập" },
        { style: "min-width: 235px;", label: "Trạng thái" },
      ],
      //lưu property trong table
      tdList: [
        { property: "title" },
        { property: "price", fun: 6 },
        { property: "rangeProcess", fun: 4 },
        { property: "imageUrl", fun: 70 },
        { property: "isActive", fun: 7 },
        { property: "id", style: "display: none" },
      ],
      TableFun: [
        { name: "Xóa", id: 1 },
        { name: "Đổi trạng thái", id: 2 },
        { name: "Nhân bản", id: 3 },
      ],
      listFilter: [],
      //lưu giá trị của filter trạng thái
      valueFilter: null,
      //lưu giá trị biding hiển thị
      BindingFilterValue: [],
      //lưu loại filter
      keywordFilter: "",
      isOpenImg: false,
      urlImage: null,
      ItemStatus: [],
      ItemPrice: [],
      ItemRange: [],
      searchFilter: null,
      searchLabel: null,
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
.excel {
  display: flex;
  width: 30px;
  height: 30px;
  font-size: 35px;
  margin-left: 10px;
  color: #bbbbbb;
}
</style>
