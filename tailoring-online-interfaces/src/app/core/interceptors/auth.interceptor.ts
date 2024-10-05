import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  let authReq = req;
  const token = localStorage.getItem('auth-token');

  if (token != null) {

    authReq = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) });

  } 
  // else {
  //   const basicAuth = 'Basic ' + btoa('admin:admin');
  //   authReq = authReq.clone({
  //     headers: req.headers.set('Authorization', basicAuth)
  //   });
  // }
  return next(authReq);
};
