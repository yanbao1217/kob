import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore('user', () => {
    const id = ref("");
    const username = ref("");
    const photo = ref("");
    const accessToken = ref("");

    function isLogin() {
        return !!accessToken.value;
    }

    function setUserInfo(data) {
        id.value = data.id;
        username.value = data.username;
        photo.value = data.photo
    }

    function setAccessToken(data) {
        accessToken.value = data.token;
    }

    function logout() {
        id.value = 0;
        username.value = "";
        photo.value = "";
        accessToken.value = "";
    }

    return {
        id,
        username,
        photo,
        accessToken,
        isLogin,
        setUserInfo,
        setAccessToken,
        logout,
    }
})