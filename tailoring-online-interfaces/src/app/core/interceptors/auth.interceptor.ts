import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let authReq = req;
  const token = localStorage.getItem('auth-token');

  if (token != null) {

    authReq = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) });

  } else {
    authReq = req.clone({ headers: req.headers.set('Authorization', `Basic YWRtaW46YWRtaW4=`) });
  }

  return next(authReq);
};
