import React from 'react';
import { Form } from 'formik';

function renderField(label, type, name, formikProps) {
  const { values, errors, touched, setFieldValue, handleBlur } = formikProps;
  return (
    <div className="form-group row">
      <label htmlFor={name} className="col-sm-6 col-form-label">{label}</label>

      <div className="col-sm-6">
        <input
          type={type}
          name={name}
          className={`form-control ${
            touched[name] && errors[name] ? 'is-invalid' : ''
          }`}
          onBlur={handleBlur}
          onChange={e => {
            const target = e.target;
            e.preventDefault();
            setFieldValue(target.name, target.value);
          }}
          value={values[name]}
        />
        <div className="invalid-feedback">{errors[name]}</div>
      </div>
    </div>
  );
}

function CreditForm(props) {
  const { formikProps, disabled } = props;
  const { values, isSubmitting, isValid } = formikProps;

  return (
    <Form
      noValidate
      className={`credit-form-container ${disabled ? 'credit-form-container__not-initialized' : ''}`}
    >
      {renderField('Сумма кредита', 'number', 'sum', formikProps)}
      {renderField('Срок кредита (мес)', 'number', 'monthCount', formikProps)}

      <div className="form-group row">
        <label className="col-sm-6 col-form-label">Годовая процентная ставка (%):</label>
        <div className="col-sm-6">{values.rate}</div>
      </div>

      <div className="form-group row">
        <label className="col-sm-6"/>
        <div className="col-sm-6">
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting || !isValid}
          >
            Рассчитать
          </button>
        </div>
      </div>
    </Form>
  );
}

export default CreditForm;
