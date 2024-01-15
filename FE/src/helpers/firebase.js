import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyCabFMOhNczLqeZ0TVrnLQGrtr5F_9tmis",
  authDomain: "vue-authentication-5990a.firebaseapp.com",
  databaseURL:
    "https://vue-authentication-5990a-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "vue-authentication-5990a",
  storageBucket: "vue-authentication-5990a.appspot.com",
  messagingSenderId: "764531761354",
  appId: "1:764531761354:web:51403dbfd9d7d259c2d433",
  measurementId: "G-YG4F9FMR3R",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const storage = getStorage(app);
export { storage };
