import { UserIcon } from '@heroicons/react/24/solid'; 

const UserAvatar = ({ user, className="" }: any) => {
  const commonClasses = "h-6 w-6 rounded-full object-cover";

  if (user?.image) {
    return (
      <img
        className={`${commonClasses} ${className}`}
        src={user.image || "https://hrms-media-dev-hp.s3.eu-north-1.amazonaws.com/Tickets/01604050-ee6f-4112-b77b-a68982b0173f_user-placeholder.jpg"}
        alt={user.name || "User Avatar"}
      />
    );
  }

  return (
    <UserIcon className={`${commonClasses} ${className} text-gray-700`} />
  );
};

export default UserAvatar;
