<template>
  <div
    class="header"
    @mouseleave="
      (isOpen = false), (isOpenManagement = false), (isOpenCart = false)
    "
  >
    <div class="header-left">
      <img src="../../assets/icons/logo.png" alt="logo" srcset="" height="90" />
    </div>
    <div class="header-center">
      <router-link
        class="header-center-item"
        to="/home"
        active-class="header-center-item_active"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="header-center-icon"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path d="M5 12l-2 0l9 -9l9 9l-2 0"></path>
          <path d="M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7"></path>
          <path d="M10 12h4v4h-4z"></path>
        </svg>
        <span class="nav-link-center">Trang chủ</span>
      </router-link>
      <router-link
        class="header-center-item"
        to="/courses"
        active-class="header-center-item_active"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="header-center-icon"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path d="M3 19a9 9 0 0 1 9 0a9 9 0 0 1 9 0"></path>
          <path d="M3 6a9 9 0 0 1 9 0a9 9 0 0 1 9 0"></path>
          <path d="M3 6l0 13"></path>
          <path d="M12 6l0 13"></path>
          <path d="M21 6l0 13"></path>
        </svg>
        <span class="nav-link-center">Khóa tập</span>
      </router-link>
      <router-link
        class="header-center-item"
        to="/ptpage"
        active-class="header-center-item_active"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="header-center-icon"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path d="M12 13a3 3 0 1 0 0 -6a3 3 0 0 0 0 6z"></path>
          <path
            d="M6.201 18.744a4 4 0 0 1 3.799 -2.744h4a4 4 0 0 1 3.798 2.741"
          ></path>
          <path
            d="M19.875 6.27c.7 .398 1.13 1.143 1.125 1.948v7.284c0 .809 -.443 1.555 -1.158 1.948l-6.75 4.27a2.269 2.269 0 0 1 -2.184 0l-6.75 -4.27a2.225 2.225 0 0 1 -1.158 -1.948v-7.285c0 -.809 .443 -1.554 1.158 -1.947l6.75 -3.98a2.33 2.33 0 0 1 2.25 0l6.75 3.98h-.033z"
          ></path>
        </svg>
        <span class="nav-link-center">PT</span>
      </router-link>
      <router-link
        class="header-center-item"
        to="/branchdetails"
        active-class="header-center-item_active"
        @mouseover="isOpen = true"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="header-center-icon"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path d="M2 12h1"></path>
          <path d="M6 8h-2a1 1 0 0 0 -1 1v6a1 1 0 0 0 1 1h2"></path>
          <path
            d="M6 7v10a1 1 0 0 0 1 1h1a1 1 0 0 0 1 -1v-10a1 1 0 0 0 -1 -1h-1a1 1 0 0 0 -1 1z"
          ></path>
          <path d="M9 12h6"></path>
          <path
            d="M15 7v10a1 1 0 0 0 1 1h1a1 1 0 0 0 1 -1v-10a1 1 0 0 0 -1 -1h-1a1 1 0 0 0 -1 1z"
          ></path>
          <path d="M18 8h2a1 1 0 0 1 1 1v6a1 1 0 0 1 -1 1h-2"></path>
          <path d="M22 12h-1"></path>
        </svg>
        <span class="nav-link-center">Phòng tập</span>
        <m-drop-menu
          @mouseleave="isOpen = false"
          :items="Branches"
          v-if="isOpen"
        ></m-drop-menu>
      </router-link>
      <router-link
        class="header-center-item"
        to="/blogspage"
        active-class="header-center-item_active"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="header-center-icon"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path
            d="M3 4m0 2a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2z"
          ></path>
          <path d="M7 8h10"></path>
          <path d="M7 12h10"></path>
          <path d="M7 16h10"></path>
        </svg>
        <span class="nav-link-center">Bài viết</span>
      </router-link>
      <router-link
        class="header-center-item"
        to="/orderuser"
        active-class="header-center-item_active"
        @mouseover="isOpenManagement = true"
        v-if="roleValue == 'USER'"
      >
        <i class="ti ti-calendar-event"></i>
        <span class="nav-link-center">Quản lý</span>
        <m-drop-menu
          @mouseleave="isOpenManagement = false"
          :items="Management"
          v-if="isOpenManagement"
        ></m-drop-menu>
      </router-link>
      <router-link
        class="header-center-item"
        to="/admin/users"
        active-class="header-center-item_active"
        v-if="
          roleValue == 'ADMIN' || roleValue == 'MANAGER' || roleValue == 'STAFF'
        "
      >
        <i class="ti ti-calendar-event"></i>
        <span class="nav-link-center">Quản lý phòng tập</span>
      </router-link>
      <router-link
        class="header-center-item"
        to="/admin/registeduser"
        active-class="header-center-item_active"
        v-if="roleValue == 'MENTOR'"
      >
        <i class="ti ti-calendar-event"></i>
        <span class="nav-link-center">Quản lý lịch dạy</span>
      </router-link>
    </div>
    <div class="header-right">
      <div
        class="header-right-item-sidebar"
        active-class="header-center-item_active"
        @click="openSiderbarPopup"
      >
        <i class="ti ti-layout-sidebar-left-collapse-filled"> </i>
      </div>
      <div class="header-right-dropdown">
        <MDropItems
          v-if="isDropItems"
          :MDropSta="4"
          :Inputstyle="'border: none; height: 40px; background-color: #fff'"
          :Drstyle="'margin-top: 15px; width: 250px; max-height: 250px'"
          :label="'name'"
          :url="'logoUrl'"
          :DropItemsModel="selectedBranch.id"
          :RecordDrop="Branches"
          @dropitem-value="getBranch"
        />
      </div>
      <div class="header-right-item search">
        <svg
          @click="toggleSearch"
          xmlns="http://www.w3.org/2000/svg"
          class="icon icon-tabler icon-tabler-search"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          stroke-width="2"
          stroke="currentColor"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
          <path d="M10 10m-7 0a7 7 0 1 0 14 0a7 7 0 1 0 -14 0"></path>
          <path d="M21 21l-6 -6"></path>
        </svg>
        <div class="search-bar">
          <input
            v-if="isShowSearchBar"
            id="searchBar"
            name="search"
            type="search"
            placeholder="Search"
            v-model="inputSearch"
            @input="filteredCourses"
          />
          <div class="search-result" v-if="isShowSearchBar && searchResults">
            <ul>
              <li
                v-for="course in searchResults"
                :key="course.id"
              ><a :href="`/courses/${course.id}`">{{ course.title }}</a>
                
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="header-right-item" v-if="isAuthenticated">
        <router-link to="/cartpage" @mouseover="isOpenCart = true">
          <MTooltip :content="'Đăng ký của tôi'" />
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="icon icon-tabler icon-tabler-shopping-cart-filled"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            stroke-width="2"
            stroke="currentColor"
            fill="none"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
            <path
              d="M6 2a1 1 0 0 1 .993 .883l.007 .117v1.068l13.071 .935a1 1 0 0 1 .929 1.024l-.01 .114l-1 7a1 1 0 0 1 -.877 .853l-.113 .006h-12v2h10a3 3 0 1 1 -2.995 3.176l-.005 -.176l.005 -.176c.017 -.288 .074 -.564 .166 -.824h-5.342a3 3 0 1 1 -5.824 1.176l-.005 -.176l.005 -.176a3.002 3.002 0 0 1 1.995 -2.654v-12.17h-1a1 1 0 0 1 -.993 -.883l-.007 -.117a1 1 0 0 1 .883 -.993l.117 -.007h2zm0 16a1 1 0 1 0 0 2a1 1 0 0 0 0 -2zm11 0a1 1 0 1 0 0 2a1 1 0 0 0 0 -2z"
              stroke-width="0"
              fill="currentColor"
            ></path>
          </svg>
        </router-link>
      </div>
      <div class="header-right-item" v-if="isAuthenticated">
        <router-link to="/profile">
          <MTooltip :content="'Trang cá nhân'" />
          <i class="ti ti-user-circle"></i>
        </router-link>
      </div>
      <router-link
        class="nav-link-right"
        to="/login"
        active-class="login_active"
        v-if="!isAuthenticated"
        >Đăng nhập</router-link
      >
      <div v-if="isAuthenticated" class="nav-link-right" @click="onLogout()">
        Đăng xuất
      </div>
    </div>
    <MCart v-if="isOpenCart" @mouseleave="isOpenCart = false" />
    <SiderbarPopup
      :roleValue="roleValue"
      :isAuthenticated="isAuthenticated"
      @to-logout="onLogout()"
      @close-Sider-bar="closeSiderbar"
      :style="{
        right: sidebarVisible ? '0' : '-100%',
        display: sidebarVisible ? 'block' : 'none',
        opacity: sidebarVisible ? '1' : '0',
      }"
    />
  </div>
</template>
<script>
import MDropItems from "@/components/MDropItems/MDropItems.vue";
import MDropMenu from "@/components/MDropMenu/MDropMenu.vue";
import fireStorage from "@/plugins/fireStorage";
import MCart from "@/components/MCart/MCart.vue";
import SiderbarPopup from "@/components/TheHeader/SiderbarPopup.vue";
import MTooltip from "@/components/MTooltip/MTooltip.vue";
import apiConfig from "@/api/config/apiConfig";
import fetchOptions from "@/api/base/fetchOptions";
import {
  IS_AUTHENTICATE_GETTER,
  LOGOUT_ACTION,
} from "@/stores/modules/storeconstants";
import { mapActions, mapGetters } from "vuex";
import { SET_BRANCH_HOME_SELECTED } from "@/stores/modules/storeconstants";
import { mapMutations } from "vuex";
export default {
  components: {
    MDropMenu,
    MCart,
    MTooltip,
    MDropItems,
    SiderbarPopup,
  },
  created() {
    (async () => {
      try {
        await this.loadBranch();
        this.branchIdGuest = localStorage.getItem("branchIdGuest");
        this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
        this.selectedBranch.id = JSON.parse(localStorage.getItem("userData"))[
          "branchId"
        ];
        if (this.roleValue == "ADMIN" || this.roleValue == null) {
          this.selectedBranch.id = this.branchIdGuest;
        }
        this.loadAllCourses();
      } catch (error) {
        console.log(error);
      }
    })();
  },

  updated() {
    try {
      this.roleValue = JSON.parse(localStorage.getItem("userData"))["role"];
      this.selectedBranch.id = JSON.parse(localStorage.getItem("userData"))[
        "branchId"
      ];
      if (this.roleValue == "ADMIN" || this.roleValue == null) {
          this.selectedBranch.id = this.branchIdGuest;
        }
    } catch (error) {
      console.log(error);
    }
  },
  methods: {
    ...mapActions("auth", {
      logout: LOGOUT_ACTION,
    }),
    ...mapMutations({
      setBranch: SET_BRANCH_HOME_SELECTED,
    }),

    openSiderbarPopup() {
      this.sidebarVisible = true;
    },
    closeSiderbar() {
      this.sidebarVisible = false;
    },

    onLogout() {
      var rememberMe = localStorage.getItem("rememberMe");
      if (rememberMe === "false") {
        localStorage.removeItem("userName");
        localStorage.removeItem("password");
        localStorage.removeItem("rememberMe");
      }
      localStorage.removeItem("branchIdGuest");
      this.selectedBranch.id = null;
      this.logout();
    },

    /**
     * Hàm lấy branch
     */
    getBranch(branchid) {
      this.selectedBranch.id = branchid;
      this.setBranch({
        branch: this.Branches.find((branch) => {
          return branch.id == branchid;
        }),
      });
      if (this.roleValue == "ADMIN" || this.roleValue == null) {
        localStorage.setItem("branchIdGuest", branchid);
      }
      this.loadAllCourses();
    },
    /**
     * hàm load dữ liệu
     */
    async loadBranch() {
      fetch(`${apiConfig}/branches`, await fetchOptions("GET"))
        .then((data) => data.json().catch(() => data))
        .then(async (data) => {
          this.Branches = data.content; //lưu dữ liệu
          if (this.Branches.length > 0 && this.selectedBranch.id == "NOT_YET" || this.Branches.length > 0 && this.selectedBranch.id == null ) {
            this.selectedBranch.id = this.Branches[0].id;
            this.selectedBranch.name = this.Branches[0].name;
            if (this.branchIdGuest != null ) {
              this.selectedBranch.id = this.branchIdGuest;
            }
          }
          await this.Branches.map(async (branch) => {
             branch.logoUrl = await fireStorage.displayImage(branch.logoUrl);
            });
          this.isDropItems = true;
        })
        .catch((res) => {
          console.log(res);
        });
    },
    toggleSearch() {
      this.isShowSearchBar = !this.isShowSearchBar;
      this.inputSearch = null;
    },
    async loadAllCourses() {
      try {
        fetch(
          `${apiConfig}/courses?branchId=${this.selectedBranch.id}`,
          await fetchOptions("GET")
        )
          .then((response) => response.json().catch(() => response))
          .then((response) => {
            if (response.status && response.status != 200) {
              console.log(response);
            } else {
              this.listAllCourse = response.content;
            }
            this.showLoading = false;  
          })
          .catch((error) => {
            console.log(error);
          });
      } catch (error) {
        console.log(error);
      }
    },
    filteredCourses() {
      if (!this.inputSearch) {
        this.searchResults = null;
      } else {
        const query = this.inputSearch.toLowerCase();
        this.searchResults = this.listAllCourse.filter((course) => {
          const title = course.title.toLowerCase();
          return title.includes(query);
        });
        this.searchResults = this.searchResults.slice(0, 5);
      }
    },
    redirectResult(id) {
      this.$router.push({ path: `/courses/${id}` });
    },
  },
  data() {
    return {
      isLogin: false,
      isDropItems: false,
      Branches: [],
      searchResults: {},
      listAllCourse: [],
      inputSearch: "",
      Management: [
        { name: "Tài khoản của tôi", linkRouter: "/profile" },
        { name: "Quản lý lịch tập", linkRouter: "/usercourse" },
        { name: "Lịch sử đăng ký", linkRouter: "/orderuser" },
      ],
      roleValue: null,
      selectedBranch: {
        id: null,
        name: null,
      },
      branchIdGuest: null,
      isOpen: false,
      isOpenManagement: false,
      isOpenCart: false,
      isShowSearchBar: false,
      sidebarVisible: false,
    };
  },
  computed: {
    ...mapGetters("auth", {
      isAuthenticated: IS_AUTHENTICATE_GETTER,
    }),
  },
};
</script>
<style scoped>
a {
  text-decoration: none;
}
.header {
  width: 100%;
  height: 100px;
  float: left;
  clear: right;
  background-color: #ffff;
  display: flex;
  background: #fff;
  justify-content: space-between;
  box-shadow: 0px 5px 15px 0px rgb(0 33 85 / 10%);
}
.header-left {
  width: 14%;
  display: flex;
  float: left;
  align-items: center;
}
.header-left::after {
  content: "Gym Management System";
  font-size: 1.3em;
  font-weight: bold;
  text-align: left;
  font-family: cursive;
}
.header-center {
  width: calc(100% - 29% - 0% - 14%);
  display: flex;
  align-items: center;
  justify-content: space-evenly;
}

.header-center-item {
  display: flex;
  padding: 10px;
  position: relative;
  font-size: 1.2em;
  align-items: flex-end;
  color: black;
  border-bottom: 2px solid transparent;
  transition: 0.2s ease-in-out;
}
.header-center-icon {
  margin-right: 2px;
}
.header-right-item-sidebar {
  display: none;
  padding: 10px;
  position: relative;
  font-size: 30px;
  align-items: flex-end;
  color: black;
  border-bottom: 2px solid transparent;
  transition: 0.2s ease-in-out;
  color: #fff;
}
.header-right {
  width: 29%;
  display: flex;
  float: right;
  align-items: center;
  background-color: #e9485a;
  padding: 0 20px;
}
.header-right-item {
  margin: 0 15px;
  font-size: 24px;
  position: relative;
}
.header-right-item.search {
  position: relative;
}
.header-right-item.search:hover {
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 5px 5px 0 0;
}
.header-right-dropdown {
  width: 180px;
  height: 100%;
  padding: 20px 0;
}
.dropdown-container {
  display: flex;
  background-image: initial;
  background-color: rgba(0, 0, 0, 0.2);
}
.ti.ti-user-circle,
.ti.ti-message {
  color: #2c3e50;
}
.nav-link-right {
  text-decoration: none;
  font-weight: 100;
  color: white;
  padding: 7px 12px;
  border: 1px solid #fff;
  border-radius: 20px;
}
.nav-link-right:hover {
  box-shadow: rgba(0, 0, 0, 0.25) 0px 14px 28px,
    rgba(0, 0, 0, 0.22) 0px 10px 10px;
  transition: 0.3s;
}
.login_active {
  background-color: rgba(237, 128, 137, 0.5);
}
.login_active:hover {
  box-shadow: none;
  cursor: default;
}
.header-center-item:hover {
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 5px 5px 0 0;
}
.nav-link-right {
  cursor: pointer;
}
.ti.ti-calendar-event {
  font-size: 25px;
}
.header-center-item_active {
  color: #e9485a;
  border-color: #e9485a;
  cursor: default;
}
.header-center-item_active:hover {
  background-color: transparent;
}

/* searchbar CSS  */

.search-bar {
  position: relative;
}
#searchBar {
  position: absolute;
  left: -30px;
  top: 105%;
  background-color: white;
  font-size: 14px;
  font-weight: 100;
  padding: 0.25em 0.75em;
  max-width: 130px;
  text-align: left;
  border-radius: 28px;
}
.search-result {
  position: absolute;
  top: 32px;
  left: -50px;
  min-width: 200px;
}
.search-bar ul {
  padding: 0;
  list-style: none;
}
.search-result ul li {
  font-weight: normal;
  font-size: 16px;
  background-color: #f5f5f5;
  height: 50px;
  display: flex;
  align-items: center;
  padding-left: 15px;
  cursor: pointer;
}
.search-result li:hover {
  background-color: rgb(204 227 227);
}

@media (max-width: 1200px) {
  .header-left {
    display: none;
  }
  .nav-link-center {
    display: none;
  }
  .header-right {
    width: 20%;
    padding: 17px;
  }
  .header-center {
    width: calc(85% - 0% - 0% - 0%);
  }
  .header-right-item {
    display: none;
  }
  .header-right-dropdown {
    display: none;
  }
  .nav-link-right {
    display: none;
  }
  .header-right-item-sidebar {
    display: flex;
  }
}
</style>
