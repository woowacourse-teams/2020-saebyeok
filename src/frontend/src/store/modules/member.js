import { DELETE_MEMBER } from '@/store/shared/actionTypes';

import MemberService from '@/api/modules/member';

const actions = {
  async [DELETE_MEMBER]() {
    return MemberService.delete();
  }
};

export default {
  actions
};
