<script setup>
import Card from '@/components/Card.vue';
import api from '@/js/http/api';
import { pkStore } from '@/stores/pk';
import { useRecordStore } from '@/stores/record';
import { useUserStore } from '@/stores/user';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const page = ref(1);
const total_page = ref(0);
const user = useUserStore();
const record_counts = ref(0);
const records = ref([]);
const router = useRouter();
const myRecord = useRecordStore();
const pk = pkStore();

onMounted(() => {
    pull_page();
});

async function pull_page() {
    try {
        const res = await api.get("http://127.0.0.1:8080/record/getlist/", {
            params: {
                page: page.value
            },
            headers: {
                Authorization: `Bearer ${user.accessToken}`
            }
        });

        const data = res.data;
        record_counts.value = data.records_count;
        records.value = data.records;
        total_page.value = data.total_page;
        page.value = data.current_page;
    } catch (err) {
        console.log(err);
    }
}

function click_prev() {
    if (page.value > 1) {
        page.value--;
        pull_page();
    }
}

function click_next() {
    if (page.value < total_page.value) {
        page.value++;
        pull_page();
    }
}

async function open_record_content(recordId) {
    try {
        for (const record of records.value) {
            if (record.record.id === recordId) {
                myRecord.updateIsRecord(true);
                pk.updateGame({
                    map: stringTo2D(record.record.map),
                    a_id: record.record.aId,
                    a_sx: record.record.aSx,
                    a_sy: record.record.aSy,
                    b_id: record.record.bId,
                    b_sx: record.record.bSx,
                    b_sy: record.record.bSy,
                });
                myRecord.updateSteps({
                    a_steps: record.record.aSteps,
                    b_steps: record.record.bSteps,
                });
                myRecord.updateRecordLoser(record.record.loser);
                router.push({
                    name: "record_content",
                    params: {
                        recordId
                    }
                });
                break;
            }
        }
    } catch (err) {
        console.log(err);
    }
}

function stringTo2D(map) {
    let g = [];
    for (let i = 0, k = 0; i < 14; ++i) {
        let line = [];
        for (let j = 0; j < 15; ++j, ++k) {
            if (map[k] === '0') line.push(0);
            else line.push(1);
        }
        g.push(line);
    }
    return g;
}
</script>

<template>
    <div class="flex justify-center mt-2">
        <Card class="w-full max-w-4xl">
            <div>
                <div class="card flex justify-center">
                    <div class="card-header flex justify-center mt-4">
                        <span>对局信息</span>
                    </div>

                    <div class="card-body">
                        <div class="overflow-x-auto">
                            <table class="table table-zebra">
                                <thead>
                                    <tr>
                                        <th>A</th>
                                        <th>B</th>
                                        <th>对战结果</th>
                                        <th>对战时间</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="record in records" :key="record.record.id">
                                        <td>
                                            <img :src="record.a_photo" alt="" class="w-10 h-10 rounded-full inline-block mr-2" />
                                            <span>{{ record.a_username }}</span>
                                        </td>
                                        <td>
                                            <img :src="record.b_photo" alt="" class="w-10 h-10 rounded-full inline-block mr-2" />
                                            <span>{{ record.b_username }}</span>
                                        </td>
                                        <td>
                                            <span>{{ record.result }}</span>
                                        </td>
                                        <td>
                                            <span>{{ record.record.createtime }}</span>
                                        </td>
                                        <td>
                                            <button
                                                @click="open_record_content(record.record.id)"
                                                class="btn btn-sm w-32 text-gray-500 text-sm"
                                            >
                                                查看录像
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="mt-4 text-center">
                            共 {{ record_counts }} 条记录，当前第 {{ page }} / {{ total_page }} 页
                        </div>
                    </div>
                </div>
            </div>

            <div class="join flex justify-center my-4">
                <button
                    class="join-item btn"
                    @click="click_prev"
                    :disabled="page <= 1"
                >
                    «
                </button>

                <button class="join-item btn">
                    {{ page }}
                </button>

                <button
                    class="join-item btn"
                    @click="click_next"
                    :disabled="page >= total_page"
                >
                    »
                </button>
            </div>
        </Card>
    </div>
</template>

<style scoped>
</style>