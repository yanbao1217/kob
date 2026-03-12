import { defineStore } from "pinia";
import { ref } from "vue";

export const useRecordStore = defineStore('record', () => {
    const is_record = ref(false);
    const a_steps = ref("")
    const b_steps = ref("")
    const record_loser = ref("")

    function updateIsRecord(new_is_record) {
        is_record.value = new_is_record;
    }

    function updateSteps(data) {
        a_steps.value = data.a_steps
        b_steps.value = data.b_steps;
    }

    function updateRecordLoser(newRecordLoser) {
        record_loser.value = newRecordLoser
    }

    return {
        is_record,
        a_steps,
        b_steps,
        record_loser,
        updateIsRecord,
        updateSteps,
        updateRecordLoser
    }
})