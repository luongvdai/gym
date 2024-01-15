import { storage } from "@/helpers/firebase";
import {
  ref,
  uploadBytes,
  getDownloadURL,
  deleteObject,
} from "firebase/storage";

const fireStorage = {
  getURL: function (folder, name, image) {
    try {
      if (!image) return "";
      const metadata = {
        contentType: image.type,
      };
      return new Promise((resolve, reject) => {
        try {
          const storageRef = ref(storage, `${folder}/${name + image.name}`);
          uploadBytes(storageRef, image, metadata).then(() => {
            resolve(`${folder}/${name + image.name}`);
          });
        } catch (error) {
          reject("Không upload thành công");
        }
      });
    } catch (error) {
      console.log(error);
    }
  },
  deleteImage: async function (path) {
    try {
      const storageRef = ref(storage, path);
      return await deleteObject(storageRef)
        .then(() => {
          console.log("xóa thành công");
        })
        .catch(() => {
          console.log("xóa thất bại");
        });
    } catch (error) {
      console.log("Image not found");
    }
  },
  displayImage: async function (path) {
    try {
      if (path === "") {
        return Promise.resolve(""); // Trả về giá trị mặc định (chuỗi rỗng) nếu path là rỗng
      }

      const storageRef = await ref(storage, path);
      return await getDownloadURL(storageRef).catch(() => "");
    } catch (error) {
      const storageRef = await ref(storage, "error/notfound.png");
      return await getDownloadURL(storageRef).catch(() => "");
    }
  },
};

export default fireStorage;
