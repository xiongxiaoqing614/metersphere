<template>
  <test-case-relevance-base
    @setProject="setProject"
    @save="saveCaseRelevance"
    ref="baseRelevance"
  >
    <template v-slot:aside>
      <ms-api-module
        :relevance-project-id="projectId"
        @nodeSelectEvent="nodeChange"
        @protocolChange="handleProtocolChange"
        @refreshTable="refresh"
        @setModuleOptions="setModuleOptions"
        :is-read-only="true"
        ref="nodeTree"
      />
    </template>
    <review-relevance-api-list
      v-if="isApiListEnable"
      :current-protocol="currentProtocol"
      :select-node-ids="selectNodeIds"
      :is-api-list-enable="isApiListEnable"
      :project-id="projectId"
      :is-test-plan="true"
      :review_id="reviewId"
      @isApiListEnableChange="isApiListEnableChange"
      ref="apiList"/>

    <review-relevance-case-list
      v-if="!isApiListEnable"
      :current-protocol="currentProtocol"
      :select-node-ids="selectNodeIds"
      :is-api-list-enable="isApiListEnable"
      :project-id="projectId"
      :is-test-plan="true"
      :review-id="reviewId"
      @isApiListEnableChange="isApiListEnableChange"
      ref="apiCaseList"/>


  </test-case-relevance-base>
</template>

<script>
import TestCaseRelevanceBase from "@/business/components/track/plan/view/comonents/base/TestCaseRelevanceBase";
import MsApiModule from "@/business/components/api/definition/components/module/ApiModule";
import ReviewRelevanceApiList from "@/business/components/track/review/view/components/ReviewRelevanceApiList";
import ReviewRelevanceCaseList from "@/business/components/track/review/view/components/ReviewRelevanceCaseList";

export default {
  name: "TestReviewRelevanceApi",
  components: {ReviewRelevanceCaseList, ReviewRelevanceApiList, MsApiModule, TestCaseRelevanceBase},
  data() {
    return {
      showCasePage: true,
      currentProtocol: null,
      currentModule: null,
      selectNodeIds: [],
      moduleOptions: {},
      trashEnable: false,
      isApiListEnable: true,
      condition: {},
      currentRow: {},
      projectId: ""
    };
  },
  props: {
    reviewId: {
      type: String
    }
  },
  watch: {
    reviewId() {
      this.condition.reviewId = this.reviewId;
    },
  },
  methods: {
    open() {
      this.init();
      this.$refs.baseRelevance.open();
    },
    init() {
      if (this.$refs.apiList) {
        this.$refs.apiList.initTable();
      }
      if (this.$refs.apiCaseList) {
        this.$refs.apiCaseList.initTable();
      }
      if (this.$refs.nodeTree) {
        this.$refs.nodeTree.list();
      }
    },
    setProject(projectId) {
      this.projectId = projectId;
    },
    isApiListEnableChange(data) {
      this.isApiListEnable = data;
    },

    refresh(data) {
      if (this.isApiListEnable) {
        this.$refs.apiList.initTable(data);
      } else {
        this.$refs.apiCaseList.initTable(data);
      }
    },

    nodeChange(node, nodeIds, pNodes) {
      this.selectNodeIds = nodeIds;
    },
    handleProtocolChange(protocol) {
      this.currentProtocol = protocol;
    },
    setModuleOptions(data) {
      this.moduleOptions = data;
    },
    saveCaseRelevance() {
      let param = {};
      let url = '';
      let environmentId = undefined;
      let selectIds = [];
      if (this.isApiListEnable) {
        url = '/api/definition/relevance/review';
        environmentId = this.$refs.apiList.environmentId;
        selectIds = Array.from(this.$refs.apiList.selectRows).map(row => row.id);
      } else {
        url = '/api/testcase/relevance/review';
        environmentId = this.$refs.apiCaseList.environmentId;
        selectIds = Array.from(this.$refs.apiCaseList.selectRows).map(row => row.id);
      }

      if (!environmentId) {
        this.$warning(this.$t('api_test.environment.select_environment'));
        return;
      }

      param.reviewId = this.reviewId;
      param.selectIds = selectIds;
      param.environmentId = environmentId;

      this.result = this.$post(url, param, () => {
        this.$success(this.$t('commons.save_success'));
        this.$emit('refresh');
        this.refresh();
        this.$refs.baseRelevance.close();
      });
    },

  }
}
</script>

<style scoped>
/deep/ .select-menu {
  margin-bottom: 15px;
}

/deep/ .environment-select {
  float: right;
  margin-right: 10px;
}
</style>
