import React from 'react';
import { convertSum } from 'utils/number';
import { monthsMap } from 'utils/months';
import './schedule.css';

function Schedule(props) {
  const { data } = props;

  return (
    <table className="table schedule">
      <thead>
      <tr>
        <th scope="col">Номер платежа</th>
        <th scope="col">Месяц/Год</th>
        <th scope="col">Платеж по основному долгу</th>
        <th scope="col">Платеж по процентам</th>
        <th scope="col">Остаток основного долга</th>
        <th scope="col">Общая сумма платежа</th>
      </tr>
      </thead>
      <tbody>
      {data.map((item, index) => (
        <tr key={index}>
          <th scope="row">{index + 1}</th>
          <td>{monthsMap[item.month.toString()]} / {item.year}</td>
          <td>{convertSum(item.debtPayment)}</td>
          <td>{convertSum(item.percentPayment)}</td>
          <td>{convertSum(item.debt)}</td>
          <td>{convertSum(item.payment)}</td>
        </tr>
      ))}
      </tbody>
    </table>
  );
}

export default Schedule;
