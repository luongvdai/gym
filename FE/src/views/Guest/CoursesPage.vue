<template>
  <div class="courses-page">
    <div class="courses-page-top">
      <h1 class="text-start">Danh sách khóa tập</h1>
    </div>
    <div class="courses-page-center">
      <div class="courses-page-filter">
        <div class="advance mt-3">
          <div class="fitersearch">
            <v-btn
              append-icon="ti ti-chevron-down"
              class="my-5 text-none d-flex"
              @click="openSearch"
              :color="color"
            >
              Tìm kiếm nâng cao
            </v-btn>
            <div class="input-filter d-flex align-center justify-center">
              <v-text-field
                clearable
                clear-icon="ti ti-x"
                density="compact"
                variant="underlined"
                label="Tìm kiếm khóa tập"
                single-line
                hide-details
                @click:append-inner="onClick"
                v-model="lstFilter.keyword"
              ></v-text-field>
              <v-btn
                prepend-icon="ti ti-search"
                class="mx-3 text-none d-flex"
                :color="color"
                @click="loadAllCourses"
              >
                Tìm kiếm
              </v-btn>
            </div>
          </div>

          <v-expansion-panels>
            <v-expansion-panel>
              <v-expansion-panel-title
                ref="search"
                class="d-none"
              ></v-expansion-panel-title>
              <v-expansion-panel-text class="advance-dashboard">
                <v-btn
                  class="advance-remove text-none"
                  prepend-icon="ti ti-x"
                  :color="color"
                  rounded="xl"
                  @click="filterDefault"
                  >Xóa</v-btn
                >
                <v-row class="mt-3">
                  <v-col cols="2">
                    <v-btn-toggle
                      class="btn-border h-100"
                      v-model="newcourse"
                      divided
                      :color="color"
                    >
                      <v-btn class="text-none">Mới nhất</v-btn>
                    </v-btn-toggle>
                  </v-col>
                  <v-col cols="2">
                    <MCombobox
                      :inputLabel="'Loại khóa tập'"
                      :Drstyle="'height: 35px;'"
                      :DropboxItem="filter[3]"
                      @combobox-value="getType"
                      :Filter="true"
                      :value="'id'"
                      :label="'name'"
                      :maxlength="10"
                      ref="combobox"
                      :tab="7"
                    />
                  </v-col>
                  <v-col cols="2">
                    <MCombobox
                      :inputLabel="'Thời lượng'"
                      :Drstyle="'height: 35px;'"
                      :DropboxItem="filter[1]"
                      @combobox-value="getProcess"
                      :Filter="true"
                      :value="'key'"
                      :label="'value'"
                      :maxlength="10"
                      ref="combobox"
                      :tab="7"
                    />
                  </v-col>
                  <v-col cols="2">
                    <MCombobox
                      :inputLabel="'Giá cả'"
                      :Drstyle="'height: 35px;'"
                      :DropboxItem="filter[2]"
                      @combobox-value="getPrice"
                      :value="'key'"
                      :label="'value'"
                      :Filter="true"
                      :maxlength="10"
                      ref="combobox"
                      :tab="7"
                    />
                  </v-col>
                </v-row>
              </v-expansion-panel-text>
            </v-expansion-panel>
          </v-expansion-panels>
        </div>
      </div>
      <div class="courses-page-content">
        <div class="courses-page-content_top d-flex my-5 justify-space-between">
          <div class="courses-page-content-total"></div>
          <div class="courses-page-content-showpage">
            <h4>
              {{ totalElement || 0 }} khóa tập tại cơ sở
              <router-link class="text-decoration-none" to="/">
                {{ this.getBranch.name }}</router-link
              >
            </h4>
          </div>
        </div>
        <div class="list-courses" v-if="showLoading">
          <v-skeleton-loader
            min-width="300"
            type="image, article"
            v-for="i in 8"
            :key="i"
          ></v-skeleton-loader>
        </div>
        <div class="list-courses" v-else>
          <card-course
            v-for="course in lstCourses"
            :key="course.id"
            :course="course"
          />
        </div>
        <div class="paging" v-if="!showLoading">
          <v-pagination
            :length="pageSize"
            v-model="pagein"
            prev-icon="ti ti-chevron-left"
            next-icon="ti ti-chevron-right"
            :total-visible="7"
          ></v-pagination>
        </div>
      </div>
    </div>
    <div class="courses-page-bottom"></div>
  </div>
</template>

<script>
import fetchOptions from "@/api/base/fetchOptions";
import CardCourse from "@/components/MCard/CardCourse.vue";
import apiConfig from "@/api/config/apiConfig";
import { mapGetters, mapMutations } from "vuex";
import { TOAST_SHOW_MUTATION } from "@/stores/modules/storeconstants";
import MCombobox from "@/components/MCombobox/MCombobox.vue";
import toast from "@/resouce/toast";
import { GET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
export default {
  components: { CardCourse, MCombobox },
  name: "CoursesPage",
  data: () => ({
    showLoading: false,
    newcourse: null,
    sales: null,
    color: "#35bf22",
    lstCourses: [],
    pagein: 1,
    pageSize: 0,
    totalElement: 0,
    lstFilter: {
      keyword: "",
      rangePrice: null,
      rangeProcess: null,
      type: null,
    },
    filter: [
      [
        { key: "increase", value: "Tăng dần" },
        { key: "decrease", value: "Giảm dần" },
      ],
      [],
      [
        { key: "1", value: "ít hơn 500.000đ" },
        { key: "2", value: "500.000đ - 1.000.000đ" },
        { key: "3", value: "1.000.000đ - 2.000.000đ" },
        { key: "4", value: "nhiều hơn 2.000.000đ" },
      ],
      [],
      [],
    ],
  }),
  props: ["typeId"],
  created() {
    console.log(window.innerWidth);
    (async () => {
      if (this.typeId) {
        this.lstFilter.type = this.typeId;
      }
      await this.loadAllCourses();
      await this.loadType();
      for (let i = 1; i <= 13; i++) {
        i == 13
          ? this.filter[1].push({ key: `${i}`, value: `hơn 1 năm` })
          : this.filter[1].push({ key: `${i}`, value: `${i} tháng` });
      }
    })();
  },

  methods: {
    getType(keyType) {
      this.lstFilter.type = keyType;
    },
    getProcess(keyProcess) {
      this.lstFilter.rangeProcess = keyProcess;
    },
    getPrice(keyPrice) {
      this.lstFilter.rangePrice = keyPrice;
    },
    async filterDefault() {
      this.sales = null;
      this.newcourse = null;
      this.lstFilter = {
        keyword: "",
        rangePrice: null,
        rangeProcess: null,
        type: null,
      };
      await this.loadAllCourses();
      this.$refs.search.$el.click();
    },
    getBranch(branchId) {
      this.lstFilter.branchId = branchId;
    },
    ...mapMutations({
      Toast: TOAST_SHOW_MUTATION,
    }),
    openSearch() {
      this.$refs.search.$el.click();
    },
    async loadType() {
      try {
        await fetch(`${apiConfig}/course-types`, await fetchOptions("GET"))
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            this.filter[3] = response.content;
          })
          .catch((error) => {
            console.log(error);
          });
      } catch (error) {
        console.log(error);
      }
    },
    async loadAllCourses() {
      try {
        this.showLoading = true;
        let limit = 8;
        let where = this.lstFilter.keyword || "";
        if (this.lstFilter.type) {
          where = where + `&typeId=${this.lstFilter.type}`;
        }
        if (this.getBranch.id) {
          where = where + `&branchId=${this.getBranch.id}`;
        }
        if (this.lstFilter.rangePrice) {
          where = where + `&rangePrice=${this.lstFilter.rangePrice}`;
        }
        if (this.lstFilter.rangeProcess) {
          where = where + `&rangeProcess=${this.lstFilter.rangeProcess}`;
        }
        if (this.newcourse) {
          where = where + `&newest=true`;
        }
        let filter = `?isActive=true&keyword=${where}&size=${limit}&page=${
          this.pagein - 1
        }`;
        fetch(`${apiConfig}/courses${filter}`, await fetchOptions("GET"))
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            if (response.status && response.status != 200) {
              this.Toast({
                show: true,
                icon: toast.Toastcssicon_faild,
                css: toast.Toastcss_faild,
                color: toast.ToastMess_color_faild,
                message: "Lỗi máy chủ",
              });
            } else {
              this.lstCourses = response.content;
              this.pageSize = response.totalPages;
              this.totalElement = response.totalElements;
            }
            this.showLoading = false;
            console.log(response);
          })
          .catch((error) => {
            console.log(error.message);
            this.ShowToast(3, "Lỗi máy chủ");
            this.showLoading = false;
          });
      } catch (error) {
        console.log(error);
        this.ShowToast(3, "Lỗi hệ thống");
      }
      this.showLoading = false;
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
          message: "Thành công",
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
  computed: {
    ...mapGetters({
      getBranch: GET_BRANCH_HOME_SELECTED,
    }),
  },
  watch: {
    async pagein() {
      await this.loadAllCourses();
    },
    getBranch() {
      this.loadAllCourses();
    },
  },
};
</script>

<style scoped>
.title {
  text-align: start;
  color: #1f1f1f;
}
.list-courses {
  display: flex;
  gap: 65px;
  flex-wrap: wrap;
  padding: 0 1%;
  min-height: 50vh;
  margin: 3% 0;
}
.courses-page {
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  min-height: 100vh;
  margin: 3%;
  opacity: 0;
  animation: fadeIn 0.8s ease-out forwards;
}
.btn-border {
  border: 1px solid #35bf22;
}
.courses-page-content_top {
  border-bottom: 3px solid #35bf22;
}
.advance-dashboard {
  position: relative;
}
.advance-remove {
  position: absolute;
  right: 0;
  top: -20px;
}
.input-filter {
  width: 30%;
  margin-left: 30px;
}
.fitersearch {
  display: flex;
}
.text-decoration-none {
  font-size: larger;
}
.text-decoration-none:hover {
  color: #35bf22;
  text-decoration: underline !important;
}
.paging {
  width: 100%;
  height: 5%;
}

@media (max-width: 1200px) {
  .courses-page-content-total h4,
  .courses-page-content-total a,
  .courses-page-content-showpage h4,
  .courses-page-content-showpage a {
    font-size: 15px;
  }
  .courses-page-content-total {
    width: 30%;
  }
  .courses-page {
    width: 96%;
  }
  .v-row {
    display: block;
  }
  .v-col-2 {
    max-width: 100%;
  }
  .v-btn-group--density-default.v-btn-group {
    display: flex;
    width: 30%;
    height: 38px !important;
  }
  .advance-remove {
    top: 5px;
  }
  .v-expansion-panel-text__wrapper {
    padding: 20px 24px 16px !important;
  }
}
@media (min-width: 1899px) {
  .list-courses {
    margin: 3% 5.7%;
  }
}
@media (min-width: 1535px) {
  .list-courses * {
    width: 20%;
    display: flex;
    flex-wrap: wrap;
    margin-right: 1%;
  }
}
</style>
