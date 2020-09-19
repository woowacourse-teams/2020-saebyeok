import ApiService from '@/api';

const BASE_URL = '/member';

const Member = {
    delete() {
        return ApiService.delete(`${BASE_URL}`);
    }
};

export default Member;