import { useEffect, useState } from 'react';
import useNotifications from 'hooks/useNotifications';

function useRate() {
  const [rate, setRate] = useState(null);
  const [loading, setLoading] = useState(true);
  const { error } = useNotifications();

  useEffect(() => {
    fetch('http://localhost:8080/calculator/rate')
      .then(resp => resp.json())
      .then(data => {
        if (data.message) {
          error(data.message);
        }
        else {
          setRate(data);
        }
      })
      .catch(err => error(err.message))
      .finally(() => setLoading(false));
  }, [setRate, setLoading, error]);

  return {
    rate,
    loadingRate: loading
  };
}

export default useRate;