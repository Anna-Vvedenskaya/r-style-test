import { useMemo } from 'react';
import { NotificationManager } from 'react-notifications';

function useNotifications() {
  return useMemo(() => ({
    info: NotificationManager.info.bind(NotificationManager),
    success: NotificationManager.success.bind(NotificationManager),
    warning: NotificationManager.warning.bind(NotificationManager),
    error: NotificationManager.error.bind(NotificationManager),
  }), []);
}

export default useNotifications;