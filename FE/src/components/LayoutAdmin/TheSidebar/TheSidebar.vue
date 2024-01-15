<template>
  <div
    class="sidebar"
    :class="isSidebar === true ? 'sidebar-small' : ''"
    ref="sidebar"
  >
    <div class="sidebar-top">
      <button
        class="sidebar-btn-left sidebar-btn-left-open"
        type="button"
        @click="this.isSidebar = !this.isSidebar"
      >
        <i class="ti ti-grid-dots sidebar-open"></i>
      </button>
      <span class="sidebar-item-laber"></span>
    </div>
    <div class="sidebar-menu">
      <router-link
        v-for="index in SidebarItems"
        :key="index"
        :to="index.link"
        class="sidebar-item"
        active-class="sidebar-item_active"
      >
        <i :class="index.icon"></i>
        <span class="sidebar-item-laber">{{ index.laber }}</span>
        <span class="sidebar-tooltip">{{ index.laber }}</span>
      </router-link>

      <div
        @click="Logout"
        class="sidebar-item signout"
        active-class="sidebar-item_active"
      >
        <i class="ti ti-logout sidebar-item-icon"></i>
        <span class="sidebar-item-laber">Đăng xuất</span>
        <div class="sidebar-hover"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { LOGOUT_ACTION } from "@/stores/modules/storeconstants";
import { mapActions } from "vuex";
export default {
  name: "TheSidebarAdmin",
  created() {
    this.role = JSON.parse(localStorage.getItem("userData"))["role"];
    if (this.role == "ADMIN") {
      this.SidebarItems = this.AdminItems;
    } else if (this.role == "STAFF") {
      this.SidebarItems = this.StaffItems;
    } else if (this.role == "MANAGER") {
      this.SidebarItems = this.ManagerItems;
    } else if (this.role == "MENTOR") {
      this.SidebarItems = this.MentorItems;
    } else {
      this.SidebarItems = [];
    }
  },
  methods: {
    ...mapActions("auth", {
      logout: LOGOUT_ACTION,
    }),
    /**
     * Hàm đăng xuất
     */
    Logout() {
      this.logout();
      var rememberMe = localStorage.getItem("rememberMe");
      if (rememberMe === "false") {
        localStorage.removeItem("userName");
        localStorage.removeItem("password");
        localStorage.removeItem("rememberMe");
        if(this.role == "ADMIN"){
          localStorage.removeItem("branchIdAdmin");
        }
      }
      //this.$router.push("login");
    },
  },
  data() {
    return {
      isSidebar: false,
      role: null,
      SidebarItems: [],
      AdminItems: [
        { link: "/", icon: "ti ti-home sidebar-item-icon", laber: "Trang chủ" },
        {
          link: "/admin/dashboard",
          icon: "ti ti-layout-dashboard sidebar-item-icon",
          laber: "Tổng quan",
        },
        {
          link: "/admin/device",
          icon: "ti ti-barbell sidebar-item-icon",
          laber: "Trang thiết bị",
        },
        {
          link: "/admin/branch",
          icon: "ti ti-git-branch sidebar-item-icon",
          laber: "Chi nhánh",
        },
        {
          link: "/admin/course",
          icon: "ti ti-e-passport sidebar-item-icon",
          laber: "Khóa tập",
        },
        {
          link: "/admin/blog",
          icon: "ti ti-article sidebar-item-icon",
          laber: "Bài viết",
        },
        {
          link: "/admin/users",
          icon: "ti ti-user-circle sidebar-item-icon",
          laber: "Người dùng",
        },
        {
          link: "/admin/orders",
          icon: "ti ti-shopping-cart sidebar-item-icon",
          laber: "Đăng ký",
        },
        {
          link: "/admin/consulting",
          icon: "ti ti-mail-question sidebar-item-icon",
          laber: "Thắc mắc",
        },
        {
          link: "/admin/feedback",
          icon: "ti ti-calendar-star sidebar-item-icon",
          laber: "Đánh giá",
        },
      ],
      StaffItems: [
        { link: "/", icon: "ti ti-home sidebar-item-icon", laber: "Trang chủ" },
        {
          link: "/admin/users",
          icon: "ti ti-user-circle sidebar-item-icon",
          laber: "Người dùng",
        },
        {
          link: "/admin/orders",
          icon: "ti ti-shopping-cart sidebar-item-icon",
          laber: "Đăng ký",
        },
         {
          link: "/admin/consulting",
          icon: "ti ti-mail-question sidebar-item-icon",
          laber: "Thắc mắc",
        },
      ],
      ManagerItems: [
        { link: "/", icon: "ti ti-home sidebar-item-icon", laber: "Trang chủ" },
        {
          link: "/admin/dashboard",
          icon: "ti ti-layout-dashboard sidebar-item-icon",
          laber: "Tổng quan",
        },
        {
          link: "/admin/device",
          icon: "ti ti-barbell sidebar-item-icon",
          laber: "Trang thiết bị",
        },
        {
          link: "/admin/course",
          icon: "ti ti-e-passport sidebar-item-icon",
          laber: "Khóa tập",
        },
        {
          link: "/admin/blog",
          icon: "ti ti-article sidebar-item-icon",
          laber: "Bài viết",
        },
        {
          link: "/admin/users",
          icon: "ti ti-user-circle sidebar-item-icon",
          laber: "Người dùng",
        },
        {
          link: "/admin/orders",
          icon: "ti ti-shopping-cart sidebar-item-icon",
          laber: "Đăng ký",
        },
         {
          link: "/admin/consulting",
          icon: "ti ti-mail-question sidebar-item-icon",
          laber: "Thắc mắc",
        },
        {
          link: "/admin/feedback",
          icon: "ti ti-calendar-star sidebar-item-icon",
          laber: "Đánh giá",
        },
      ],
      MentorItems: [
        { link: "/", icon: "ti ti-home sidebar-item-icon", laber: "Trang chủ" },
        {
          link: "/admin/blog",
          icon: "ti ti-article sidebar-item-icon",
          laber: "Bài viết",
        },
        {
          link: "/admin/registeduser",
          icon: "ti ti-user-check sidebar-item-icon",
          laber: "Học viên",
        },
      ],
    };
  },
};
</script>

<style scoped>
.sidebar-top {
  display: flex;
  align-items: center;
}

.sidebar-btn-left {
  width: 30px;
  height: 30px;
  border: none;
  margin-left: 5px;
  margin-top: 10px;
  margin-bottom: 20px;
}
.sidebar {
  background-color: rgb(90, 90, 90);
  box-shadow: rgba(0, 0, 0, 0.15) 2.4px 2.4px 3.2px;
  z-index: 1;
  width: 190px;
  transition: 0.1s ease-in-out;
}
.sidebar-menu {
  height: calc(100vh - 60px);
  position: relative;
}
.sidebar-item {
  display: flex;
  text-decoration: none;
  position: relative;
  align-items: flex-end;
  justify-content: flex-start;
  color: #f1f1f1;
  padding: 10px;
  margin-bottom: 5px;
}
.sidebar-item.signout {
  position: absolute;
  bottom: 0;
  width: 100%;
}

.sidebar-item_active {
  background-color: #2ca01c;
  cursor: default;
  transition: 0.2s ease-in-out;
}
.sidebar-item:hover {
  background-color: rgba(3, 3, 3, 0.5);
}
.sidebar-item_active:hover {
  background-color: #2ca01c;
}
.sidebar-item-icon {
  margin-right: 1.2rem;
  color: #f1f1f1;
  font-size: 23px;
}
.sidebar-open {
  font-size: 35px;
  color: #f1f1f1;
  margin-right: 0px;
}
.sidebar-tooltip {
  font-size: 0.7em;
  white-space: nowrap;
  left: 30%;
  top: 100%;
  position: absolute;
  background-color: black;
  border-radius: 2px;
  padding: 2px 4px;
  z-index: 5;
  color: #fff;
  visibility: hidden;
}
.sidebar-item:hover .sidebar-tooltip {
  visibility: visible;
}
.sidebar-hover {
  visibility: hidden;
  background-color: #2ca01c;
  position: absolute;
  left: 0;
  width: 5px;
  height: 44px;
}
.router-link-active .sidebar-hover {
  visibility: visible;
}
.signout {
  cursor: pointer;
}
@media (max-width: 1200px) {
  .sidebar {
    width: 50px;
  }
  .sidebar-logo {
    display: none;
  }
  .sidebar-item {
    display: flex;
    justify-content: center;
  }
  .sidebar-item-laber {
    display: none;
  }
  .sidebar-item-icon {
    font-size: 1.2em;
    margin: 0;
  }
  .sidebar-tooltip {
    min-width: 60px;
    left: 50px;
  }
}
.sidebar-small {
  width: 50px;
}
.sidebar-small .sidebar-logo {
  display: none;
}
.sidebar-small .sidebar-item {
  display: flex;
  justify-content: center;
}
.sidebar-small .sidebar-item-laber {
  display: none;
}
.sidebar-small .sidebar-item-icon {
  font-size: 1.2em;
  margin: 0;
}
.sidebar-small .sidebar-tooltip {
  min-width: 60px;
  left: 50px;
}
</style>
