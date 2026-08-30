import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 50,
    iterations: 100,
};

export default function () {
    const params = {
        redirects: 0, 
    };
    const res = http.get('http://localhost:8080/r/gezlTD6oiA', params);
    check(res, {
        'is status 302 (redirect)': (r) => r.status === 302,
    });
}
