import React, { useCallback, useEffect, useState } from 'react';
import { Formik } from 'formik';
import * as yup from 'yup';
import useRate from 'hooks/useRate';
import useNotifications from 'hooks/useNotifications';
import Schedule from 'components/Schedule';
import CreditForm from 'components/CreditForm';

const validationSchema = yup.object().shape({
  sum: yup.number().nullable().integer()
    .test('available-values',
      'Допустимые значения от 100 000 до 5 000 000',
      value => {
        const num = Number(value);
        return !Number.isInteger(num) || (num >= 100000 && num <= 5000000);
      })
    .required(),
  monthCount: yup.number().nullable().integer()
    .test('available-values',
      'Допустимые значения от 12 до 60',
      value => {
        const num = Number(value);
        return !Number.isInteger(num) || (num >= 12 && num <= 60);
      })
    .required(),
});

const NOT_INITIALIZED = 0;
const INITIALIZED_ERROR = 1;
const INITIALIZED = 2;
const defaultValues = { rate: '', sum: 100000, monthCount: 12 };

function CalculateCredit() {
  const { rate, loadingRate } = useRate();
  const [initialValues, setInitialValues] = useState(defaultValues);
  const [initializeState, setInitializeState] = useState(NOT_INITIALIZED);
  const [schedule, setSchedule] = useState(null);
  const { error } = useNotifications();

  useEffect(() => {
    if (!loadingRate) {
      if (rate) {
        setInitialValues({ ...defaultValues, rate });
        setInitializeState(INITIALIZED);
      }
      else {
        setInitializeState(INITIALIZED_ERROR);
      }
    }
  }, [loadingRate, rate]);

  const onSubmit = useCallback((values, { setSubmitting }) => {
    setSchedule(null);
    const formattedValues = { ...values, sum: values.sum * 100 };
    fetch('http://localhost:8080/calculator/annuity_schedule', {
      method: 'POST',
      mode: 'cors',
      cache: 'no-cache',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formattedValues),
    })
      .then(resp => resp.json())
      .then(data => {
        if (data.message) {
          error(data.message);
        }
        else {
          setSchedule(data);
        }
      })
      .catch(err => error(err.message))
      .finally(() => {
        setSubmitting(false);
      });
  }, [error]);

  return (
    <>
      <div className="credit-form">
        <h3>Кредитный калькулятор</h3>
        {initializeState === NOT_INITIALIZED && (
          <div className="d-flex justify-content-center">
            <div className="spinner-border" role="status">
              <span className="sr-only">Loading...</span>
            </div>
          </div>
        )}
        {initializeState === INITIALIZED_ERROR && (
          <div className="alert alert-danger" role="alert">
            Произошла непредвиденная ошибка. Расчет недоступен.
          </div>
        )}
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          enableReinitialize={true}
          onSubmit={onSubmit}
        >{formikProps => (
          <CreditForm
            formikProps={formikProps}
            disabled={initializeState !== INITIALIZED}
          />
        )}</Formik>
      </div>
      {schedule && (
        <Schedule data={schedule}/>
      )}
    </>
  );
}

export default CalculateCredit;
