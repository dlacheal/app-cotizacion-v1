import Swal from 'sweetalert2';

export const Alert = {
  success: (msg: string, title = 'Éxito') => {
    return Swal.fire({
      title,
      text: msg,
      icon: 'success',
      confirmButtonColor: '#3085d6'
    });
  },
  error: (msg: string, title = 'Error') => {
    return Swal.fire({
      title,
      text: msg,
      icon: 'error',
      confirmButtonColor: '#d33'
    });
  },
  warning: (msg: string, title = 'Advertencia') => {
    return Swal.fire({
      title,
      text: msg,
      icon: 'warning',
      confirmButtonColor: '#3085d6'
    });
  },
  confirm: (title: string, text: string, onConfirm: () => void) => {
    Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        onConfirm();
      }
    });
  }
};
