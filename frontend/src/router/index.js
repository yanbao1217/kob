import NotFound from '@/views/errors/NotFound.vue'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import RanklistIndexView from '@/views/ranklist/RanklistIndexView.vue'
import RecordContentView from '@/views/record/RecordContentView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import CreateBot from '@/views/user/bot/component/CreateBot.vue'
import UpdateBot from '@/views/user/bot/component/UpdateBot.vue'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      redirect: "/pk/",
    },
    {
      path: "/pk/",
      name: "pk_index",
      component: PkIndexView,
    },
    {
      path: "/ranklist/",
      name: "ranklist_index",
      component: RanklistIndexView,
    },
    {
      path: "/record/",
      name: "record_index",
      component: RecordIndexView,
    },
    {
      path: "/record/:recordId/",
      name: "record_content",
      component: RecordContentView,
    },
    {
      path: "/user/bot/",
      name: "user_bot_index",
      component: UserBotIndexView,
    },
    {
      path: "/404/",
      name: "not_found",
      component: NotFound,
    },
    {
      path: "/user/account/login/",
      name: "user_account_login",
      component: UserAccountLoginView,
    },
    {
      path: "/user/account/register",
      name: "user_account_register",
      component: UserAccountRegisterView,
    },
    {
      path: "/user/update_bot/:botId/",
      name: "user_update_bot",
      component: UpdateBot,
    },
    {
      path: "/user/create_bot/",
      name: "user_create_bot",
      component: CreateBot,
    },
    {
    path: "/:pathMatch(.*)*", // 匹配任意未定义的路径
    redirect: "/404/",
  },
  ],
})

export default router
