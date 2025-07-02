import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1,
    iterations: 2,
};

const params = {
    headers: {
        'Content-Type': 'application/json',
    },
};

export default function () {
    const alunoNome = `Aluno k6 VU=${__VU} ITER=${__ITER}`;
    const businessKey = `aluno-k6-${__VU}-${__ITER}`;

    const payload = JSON.stringify({
        "variables": {
            "emailAluno": { "type": "String", "value": `davidsonifes+${__VU}${__ITER}@gmail.com` },
            "emailOrientador": { "type": "String", "value": "davidsoncsantos45@gmail.com" },
            "aluno": { "type": "String", "value": alunoNome },
            "dataDefesa": { "type": "String", "value": "16/07/2025" },
            "titulo_trabalho": { "type": "String", "value": "TCC sobre Camunda" },
            "horaDefesa": { "type": "String", "value": "19:20" },
            "bancaDefesa": { "type": "Json", "value": "[{\"idBanca\":0,\"nome\":\"Maria\",\"email\":\"davidsoncs45@gmail.com\",\"instituicao\":\"UFES\",\"minicurriculo\":\"Engenharia Mecânica\"},{\"idBanca\":0,\"nome\":\"Jose\",\"email\":\"davidsonifes@gmail.com\",\"instituicao\":\"IFES - Cariacica\",\"minicurriculo\":\"Ciencias da computacao\"}]" },
            "email": { "type": "Integer", "value": 1 },
            "localDefesa": { "type": "String", "value": "IFES - Serra, sala 905T" }
        },
        "businessKey": businessKey
    });

    const res = http.post('http://localhost:8080/engine-rest/condition', payload, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}