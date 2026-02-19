import { useSelector } from 'react-redux';
import { selectUser } from '../redux/slices/userSlice';

export const useAuthorization = () => {
  const user = useSelector(selectUser);

  const hasRole = (requiredRoles: any) => {
    if (!user || !user.roles) return false;
    return requiredRoles.some((role: any) => user.roles.some((roleObj: any) => role==roleObj.name));
  };

  const isOwner = (resourceOwnerId: any) => {
    if (!user) return false;
    return user.id === resourceOwnerId;
  };

  const canAccess = (requiredRoles: any, resourceOwnerId: any) => {
    return hasRole(requiredRoles) || (resourceOwnerId && isOwner(resourceOwnerId));
  };

  return {
    user,
    hasRole,
    isOwner,
    canAccess,
  };
};
