import { ErrorDetails, IErrorDetails } from '@/shared/error/response-detail-model';

export const getDetails = (responseDetail: string): IErrorDetails => {
  let details;
  if (responseDetail) {
    details = responseDetail.match(/{([^}]+)}/g) || [];
  }
  if (details?.length > 0) {
    details = JSON.parse(details[0]);
    return new ErrorDetails(details.error, details.error_description);
  }
  return new ErrorDetails('', responseDetail);
};
