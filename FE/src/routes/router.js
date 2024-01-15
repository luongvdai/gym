import { createRouter, createWebHistory } from "vue-router";
import HomePage from "@/views/Guest/HomePage.vue";
import notFoundPage from "@/views/Guest/NotFoundPage.vue";
import SystemAccess from "@/views/Guest/SystemAccess.vue";
import CoursesPage from "@/views/Guest/CoursesPage.vue";
import CartPage from "@/views/User/CartPage.vue";
import AdminPage from "@/views/Admin/AdminPage.vue";
import DevicePage from "@/views/Admin/DevicePage.vue";
import BranchPage from "@/views/Admin/BranchPage.vue";
import BlogPage from "@/views/Admin/BlogPage.vue";
import DashboardPageVue from "@/views/Admin/DashboardPage.vue";
import ProfilePage from "@/views/User/ProfilePage.vue";
import ManageUserPage from "@/views/Admin/ManageUserPage.vue";
import CoursePage from "@/views/Admin/CoursePage.vue";
import OrderPage from "@/views/Admin/OrderPage.vue";
import CourseDetail from "@/views/Guest/CourseDetails.vue";
import ManagementOrderPage from "@/views/User/ManagementOrderPage.vue";
import UserCoursePage from "@/views/User/UserCoursePage.vue";
import PTPage from "@/views/Guest/PTPage.vue";
import PTDetailPgae from "@/views/Guest/PTDetailPage.vue";
import BranchDetailsPage from "@/views/Guest/BranchDetailsPage.vue";
import BlogsPage from "@/views/Guest/BlogsPage.vue";
import BlogDetailPage from "@/views/Guest/BlogDetailPage.vue";
import OrderSuccessPage from "@/views/User/OrderSuccessPage.vue";
import ConsultingPage from "@/views/Admin/ConsultingPage.vue";
import UserRegisteredPage from "@/views/Admin/UserRegisteredPage.vue";
import FeedbackPage from "@/views/Admin/FeedbackPage.vue";
import store from "@/stores/modules/store";
import { IS_AUTHENTICATE_GETTER } from "@/stores/modules/storeconstants";
const routes = [
  { path: "", redirect: "/home" },
  {
    path: "/home",
    component: HomePage,
    name: "home",
    meta: { title: "Trang chủ" },
  },
  {
    path: "/:catchAll(.*)",
    component: notFoundPage,
    name: "notfound",
    meta: { title: "404 Không tìm thấy" },
  },

  {
    path: "/login",
    component: SystemAccess,
    name: "login",
    meta: { title: "Đăng nhập", auth: false },
  },
  {
    path: "/profile",
    component: ProfilePage,
    meta: { title: "Trang cá nhân", auth: true },
    name: "profile",
  },
  {
    path: "/courses",
    component: CoursesPage,
    meta: { title: "Khóa tập" },
    name: "courses",
    props: (route) => ({
      typeId: route.query.typeId,
    }),
  },
  {
    path: "/courses/:id",
    component: CourseDetail,
    name: "course-detail",
    meta: { title: "Chi tiết khóa tập" },
    children: [
      {
        path: "/notfound",
        redirect: { name: "notfound" },
      },
    ],
  },
  {
    path: "/orderuser",
    component: ManagementOrderPage,
    name: "managementorderpage",
    meta: { title: "Lịch sử đăng ký", auth: true },
  },
  {
    path: "/usercourse",
    component: UserCoursePage,
    name: "usercourse",
    meta: { title: "Quản lý lịch tập", auth: true },
  },

  {
    path: "/cartpage",
    component: CartPage,
    name: "Danh sách khóa tập đăng ký",
    meta: { title: "Danh sách khóa tập đăng ký", auth: true },
  },
  {
    path: "/ordersuccess",
    component: OrderSuccessPage,
    name: "OrderSuccessPage",
    meta: { title: "Hoàn tất đăng ký", auth: true },
    props: (route) => ({
      transactionId: route.query.transactionId,
      orderSuccess: route.query.orderSuccess === "true", // Chuyển đổi giá trị thành boolean
      bankCode: route.query.bankCode,
      amount: route.query.amount,
    }),
  },
  {
    path: "/ptpage",
    component: PTPage,
    name: "ptpage",
    meta: { title: "Danh sách huấn luyện viên" },
  },
  {
    path: "/ptpage/:id",
    component: PTDetailPgae,
    name: "ptdetailpage",
    meta: { title: "Chi tiết huấn luyện viên" },
  },
  {
    path: "/branchdetails",
    component: BranchDetailsPage,
    name: "branchdetailpage",
    meta: { title: "Danh sách chi nhánh" },
  },
  {
    path: "/blogspage",
    component: BlogsPage,
    name: "blogspage",
    meta: { title: "Blog" },
    props: (route) => ({
      typeId: route.query.typeId,
      typeName: route.query.typeName,
    }),
  },
  {
    path: "/blogspage/:id",
    component: BlogDetailPage,
    name: "blogdetailpage",
    meta: { title: "BlogDetail" },
  },

  {
    path: "/admin",
    component: AdminPage,
    name: "admin",
    meta: {
      title: "Admin Dashboard",
      auth: true,
    },
    children: [
      {
        path: "",
        redirect: { name: "admin-dashboard" },
      },
      {
        path: "dashboard",
        name: "admin-dashboard",
        component: DashboardPageVue,
        meta: {
          title: "Quản lý chung",
          role: ["ADMIN", "MANAGER"],
        },
      },
      {
        path: "device",
        component: DevicePage,
        name: "admin-device",
        meta: {
          title: "Quản lý thiết bị",
          role: ["ADMIN", "MANAGER"],
        },
      },
      {
        path: "course",
        component: CoursePage,
        name: "admin-course",
        meta: {
          title: "Quản lý khóa học",
          role: ["ADMIN", "MANAGER"],
        },
      },
      {
        path: "branch",
        component: BranchPage,
        name: "admin-branch",
        meta: {
          title: "Quản lý chi nhánh",
          role: ["ADMIN", "MANAGER"],
        },
      },
      {
        path: "blog",
        component: BlogPage,
        name: "admin-blog",
        meta: {
          title: "Quản lý bài viết",
          role: ["ADMIN", "MANAGER", "MENTOR"],
        },
      },
      {
        path: "users",
        component: ManageUserPage,
        name: "admin-user",
        meta: {
          title: "Quản lý người dùng",
          role: ["ADMIN", "STAFF", "MANAGER"],
        },
      },
      {
        path: "orders",
        component: OrderPage,
        name: "admin-order",
        meta: {
          title: "Quản lý đăng ký",
          role: ["ADMIN", "STAFF", "MANAGER"],
        },
      },
      {
        path: "consulting",
        component: ConsultingPage,
        name: "admin-consultng",
        meta: {
          title: "Quản lý thắc mắc",
          role: ["ADMIN", "STAFF", "MANAGER"],
        },
      },
      {
        path: "feedback",
        component: FeedbackPage,
        name: "admin-feedback",
        meta: {
          title: "Quản lý đánh giá",
          role: ["ADMIN", "MANAGER"],
        },
      },
      {
        path: "registeduser",
        component: UserRegisteredPage,
        name: "mentor-courses",
        meta: {
          title: "Quản lý người dùng đã đăng ký",
          role: ["MENTOR"],
        },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)",
    component: notFoundPage,
    name: "notfound",
    meta: { title: "404 Không tìm thấy" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
router.beforeEach(async (to, from, next) => {
  document.title = to.meta.title;
  const isAuthenticated =
    store.getters["auth/" + IS_AUTHENTICATE_GETTER] ||
    localStorage.getItem("userData")
      ? true
      : false;
  if ("auth" in to.meta && to.meta.auth && !isAuthenticated) {
    next("/login");
  } else if ("auth" in to.meta && !to.meta.auth && isAuthenticated) {
    alert("Sau khi login không được truy cập vào đây");
    next("");
  } else if ("role" in to.meta && isAuthenticated) {
    const userRole = JSON.parse(localStorage.getItem("userData"))["role"];
    if (to.meta.role.includes(userRole)) {
      next();
    } else {
      alert("Bạn không có quyền truy cập vào trang này");
      next("");
    }
  } else {
    window.scrollTo(0, 0);
    next();
  }
});
export default router;
