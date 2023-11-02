<template>
  <small class="invalid-feedback" v-if="customErrorText">
    {{ customErrorText }}
  </small>
  <small class="invalid-feedback" v-else-if="rules?.required?.$invalid" v-text="$t('entity.validation.required')" />
  <small
    class="invalid-feedback"
    v-else-if="rules?.minLength?.$invalid"
    v-text="$t('entity.validation.minlength', { min: rules?.minLength?.$params?.minlength })"
  />
  <small
    class="invalid-feedback"
    v-else-if="rules?.maxLength?.$invalid"
    v-text="$t('entity.validation.maxlength', { max: rules?.maxLength?.$params?.maxlength })"
  />
  <small
    class="invalid-feedback"
    v-else-if="rules?.minValue?.$invalid"
    v-text="$t('entity.validation.min', { min: rules?.minValue?.$params?.min })"
  />
  <small
    class="invalid-feedback"
    v-else-if="rules?.maxValue?.$invalid"
    v-text="$t('entity.validation.max', { max: rules?.maxValue?.$params?.max })"
  />
  <small class="invalid-feedback" v-else-if="rules?.email?.$invalid" v-text="$t('entity.validation.email')" />
  <small class="invalid-feedback" v-else-if="rules?.mobile?.$invalid" v-text="$t('entity.validation.mobile')" />
  <small class="invalid-feedback" v-else-if="rules?.sameAs?.$invalid" v-text="$t('entity.validation.sameAs')" />
  <small class="invalid-feedback" v-else-if="rules?.username?.$invalid" v-text="$t('entity.validation.username')" />
  <slot v-else name="customErrors" />
</template>

<script lang="ts" setup>
defineProps({
  rules: {
    type: Object,
    default: () => {},
  },
  customErrorText: {
    type: String,
    default: '',
  },
})
</script>

<style>
.invalid-feedback {
  color: #e24c4c;
}
</style>
