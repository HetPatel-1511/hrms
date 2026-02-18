import { UserIcon } from '@heroicons/react/24/solid'; 

const UserAvatar = ({ user, className="", bgColor="text-gray-700" }: any) => {
  const commonClasses = "h-6 w-6 rounded-full object-cover";

  if (user?.image) {
    return (
      <img
        className={`${commonClasses} ${className}`}
        src={user.image}
        alt={user.name || "User Avatar"}
      />
    );
  }

  return (
    <UserIcon className={`${commonClasses} ${className} ${bgColor}`} />
  );
};

export default UserAvatar;
