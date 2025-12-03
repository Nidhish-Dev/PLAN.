export default function Home() {
  return (
    <div className="min-h-screen bg-black flex items-center justify-center">
      <div className="text-center">

        <h1 className="text-9xl md:text-[180px] font-black tracking-tighter bg-gradient-to-r from-[#00ff88] to-[#00cc66] bg-clip-text text-transparent animate-pulse">
          PLAN.
        </h1>

        <p className="text-gray-400 text-2xl md:text-3xl font-light mt-6 tracking-wider">
          Your Smart Project Manager
        </p>

        <p className="text-gray-600 text-sm md:text-base mt-16 font-mono">
          Building the future, one task at a time.
        </p>

        <div className="absolute inset-0 -z-10 overflow-hidden">
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-[#00ff88] rounded-full blur-3xl opacity-10 animate-pulse"></div>
        </div>
      </div>
    </div>
  );
}